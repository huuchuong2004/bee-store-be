package vn.huuchuong.be_bee_store.auth_module.service.impl;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import vn.huuchuong.be_bee_store.auth_module.payload.request.*;
import vn.huuchuong.be_bee_store.auth_module.repository.PasswordResetTokenRepository;
import vn.huuchuong.be_bee_store.auth_module.repository.RefreshTokenRepository;
import vn.huuchuong.be_bee_store.base.BaseResponse;
import vn.huuchuong.be_bee_store.auth_module.error.ErrorCode;
import vn.huuchuong.be_bee_store.exception.BusinessException;
import vn.huuchuong.be_bee_store.auth_module.entity.RefreshToken;
import vn.huuchuong.be_bee_store.auth_module.entity.User;
import vn.huuchuong.be_bee_store.auth_module.payload.response.AuthResponse;
import vn.huuchuong.be_bee_store.auth_module.payload.response.CreateUserResponse;
import vn.huuchuong.be_bee_store.auth_module.payload.response.UserLoginResponse;
import vn.huuchuong.be_bee_store.auth_module.repository.UserRepository;
import vn.huuchuong.be_bee_store.auth_module.service.AuthService;
import vn.huuchuong.be_bee_store.auth_module.service.MailSenderService;
import vn.huuchuong.be_bee_store.utils.JwtUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;
import vn.huuchuong.be_bee_store.auth_module.entity.PasswordResetToken;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final MailSenderService mailSenderService;
    private final MailAsyncService mailAsyncService;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;
    private final ModelMapper modelMapper;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    @Value("${app.frontend.reset-password-url}")
    private String resetPasswordUrl;

    @Override
    public BaseResponse<AuthResponse> login(UserLoginResquest request, HttpServletRequest httpReq) {
        Optional<User> optUser = userRepository.findByUsername(request.getUsername()); //Tim kiem user
        if (optUser.isEmpty()) {
            return BaseResponse.error("Sai username hoặc password");
        }

        User user = optUser.get(); // phat hien user

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) { // phat hein user va kiem tra mat khau
            return BaseResponse.error("Sai username hoặc password");
        }
        if (optUser.get().getIsActive() == null || !optUser.get().getIsActive()) {
            return BaseResponse.error(
                    ErrorCode.ACCOUNT_NOT_ACTIVATED,
                    Map.of(
                            "nextAction", "GO_TO_ACTIVATION_PAGE",
                            "email", user.getEmail()
                    )
            );
        }


        UserLoginResponse account = modelMapper.map(user, UserLoginResponse.class);

        refreshTokenService.revokeByUsernameAndUserAgent(user.getUsername(), httpReq.getHeader("User-Agent")); // se xoa di rfresh token cu neu login

        String accessToken = JwtUtils.createAccessToken(account, httpReq); // tien hanh tao refersh va access token
        String refreshToken = JwtUtils.createRefreshToken(account, httpReq);

        refreshTokenService.create(
                user.getUsername(),
                refreshToken,
                httpReq.getHeader("User-Agent"),
                7L * 24 * 60 * 60 * 1000  // 7 ngày
        ); // tao

        AuthResponse authResponse = new AuthResponse(accessToken, refreshToken);
        return BaseResponse.success(authResponse, "Đăng nhập thành công");
    }
    @Override
    @Transactional
    public BaseResponse<String> resetPassword(ResetPasswordRequest request, HttpServletRequest httpReq) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(request.getToken())
                .orElseThrow(() -> new BusinessException("Token reset password không hợp lệ"));

        if (resetToken.isUsed()) {
            throw new BusinessException("Token reset password đã được sử dụng");
        }

        if (resetToken.getExpiryDate().isBefore(Instant.now())) {
            throw new BusinessException("Token reset password đã hết hạn");
        }

        User user = resetToken.getUser();

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        resetToken.setUsed(true);
        passwordResetTokenRepository.save(resetToken);

        refreshTokenService.revokeByUsername(user.getUsername());

        return BaseResponse.success(
                "OK",
                "Đổi mật khẩu thành công. Vui lòng đăng nhập lại."
        );
    }
    @Override
    @Transactional
    public BaseResponse<String> sendMailResetPassword(EmailResetPassword request) {
        User user = userRepository.findByUsernameAndEmail(
                request.getUsername(),
                request.getEmail()
        ).orElseThrow(() -> new BusinessException(
                "Email hoặc Username của bạn không tồn tại trên hệ thống!"
        ));

        passwordResetTokenRepository.deleteByUser(user);

        String token = generateResetPasswordToken();

        PasswordResetToken resetToken = PasswordResetToken.builder()
                .token(token)
                .user(user)
                .expiryDate(Instant.now().plus(15, ChronoUnit.MINUTES))
                .used(false)
                .build();

        passwordResetTokenRepository.save(resetToken);

        String resetLink = UriComponentsBuilder
                .fromUriString(resetPasswordUrl)
                .queryParam("token", token)
                .toUriString();

        mailAsyncService.sendResetPasswordEmailAsync(user.getEmail(), resetLink);

        return BaseResponse.success(
                "OK",
                "Nếu thông tin tài khoản hợp lệ, email đặt lại mật khẩu đã được gửi."
        );
    }
    @Override
    public BaseResponse<CreateUserResponse> register(CreateUserRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BusinessException("Username already exists");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Email already exists");
        }

        if (userRepository.existsByPhone(request.getPhone())) {
            throw new BusinessException("Phone already exists");
        }

        User user = modelMapper.map(request, User.class);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setIsActive(false);

        User savedUser = userRepository.save(user);

        CreateUserResponse response = modelMapper.map(savedUser, CreateUserResponse.class);

        String activationLink = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/v1/auth/active/{accountId}")
                .buildAndExpand(savedUser.getId())
                .toUriString();

        mailAsyncService.sendActivationEmailAsync(savedUser.getEmail(), activationLink);

        String message = "Tạo tài khoản thành công! Vui lòng kiểm tra email để kích hoạt.";

        return new BaseResponse<>(response, message);
    }

    @Override
    public BaseResponse<AuthResponse> refresh(RefreshTokenRequest request, HttpServletRequest httpReq) {
        String refreshTokenStr = request.getRefreshToken();
        if (StringUtils.isBlank(refreshTokenStr)) {
            return BaseResponse.error("Thiếu refreshToken");
        }

        try {
            // Parse JWT – check signature, type, exp
            Claims claims = JwtUtils.parseRefreshToken(refreshTokenStr);

            // Check DB – tồn tại, chưa revoke, đúng UA
            RefreshToken stored = refreshTokenService.verify(
                    refreshTokenStr,
                    httpReq.getHeader("User-Agent")
            );

            String username = claims.getSubject(); // chinh la ussername

            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User không tồn tại"));

            // 👉 Dùng ModelMapper để map User -> LoginUserResponse
            UserLoginResponse account = modelMapper.map(user, UserLoginResponse.class);

            String newAccessToken = JwtUtils.createAccessToken(account, httpReq);

            AuthResponse authResponse = new AuthResponse(newAccessToken, refreshTokenStr);
            return BaseResponse.success(authResponse, "Refresh token thành công");

        } catch (RuntimeException e) {
            return BaseResponse.error(e.getMessage());
        }
    }

    @Override
    public BaseResponse<String> logout(Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            return BaseResponse.error("Không xác định được user");
        }

        String username = authentication.getName();
        refreshTokenService.revokeByUsername(username);

        return BaseResponse.success("OK", "Đã logout");
    }

    @Override
    public String activateAccount(UUID accountId) {
        User user = userRepository.findById(accountId)
                .orElseThrow(() -> new BusinessException("User not found"));

        if (!Boolean.TRUE.equals(user.getIsActive())) {
            user.setIsActive(true);
            userRepository.save(user);
        }

        try {
            ClassPathResource resource = new ClassPathResource("templates/activation.html");
            return Files.readString(resource.getFile().toPath());
        } catch (IOException e) {
            return fallbackHtml();
        }
    }

    @Override
    public BaseResponse<String> resendActivationEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException("Không tìm thấy tài khoản với email này"));

        if (Boolean.TRUE.equals(user.getIsActive())) {
            return BaseResponse.error("Tài khoản đã được kích hoạt, không cần gửi lại email.");
        }

        String activationLink = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/v1/auth/active/{accountId}")
                .buildAndExpand(user.getId())
                .toUriString();

        BaseResponse<String> mailResult =
                mailSenderService.sendActivationEmail(user.getEmail(), activationLink);

        if (mailResult.getData() == null) {
            return BaseResponse.error("Gửi lại email kích hoạt thất bại: " + mailResult.getMessage());
        }

        return BaseResponse.success("Đã gửi lại email kích hoạt tới: " + user.getEmail(),
                "Gửi lại email kích hoạt thành công");
    }





    private String generateResetPasswordToken() {
        byte[] randomBytes = new byte[32];
        new SecureRandom().nextBytes(randomBytes);

        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(randomBytes);
    }
    private String fallbackHtml() {
        return """
        <!DOCTYPE html>
                                              <html lang="vi">
                                              <head>
                                                  <meta charset="utf-8">
                                                  <meta name="viewport" content="width=device-width, initial-scale=1.0">
                                                  <title>Kích hoạt tài khoản thành công</title>
                                                  <style>
                                                      :root {
                                                          --bg-color: #0b0f19;
                                                          --text-main: #f8fafc;
                                                          --text-muted: #64748b;
                                                          --primary: #10b981;
                                                          --primary-glow: rgba(16, 185, 129, 0.15);
                                                      }
                
                                                      body {
                                                          margin: 0;
                                                          padding: 0;
                                                          font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif;
                                                          background-color: var(--bg-color);
                                                          color: var(--text-main);
                                                          display: flex;
                                                          justify-content: center;
                                                          align-items: center;
                                                          min-height: 100vh;
                                                          overflow-x: hidden;
                                                          position: relative;
                                                      }
                
                                                      /* Vùng phát sáng ambient làm nền tạo chiều sâu không gian */
                                                      body::before {
                                                          content: "";
                                                          position: absolute;
                                                          width: 500px;
                                                          height: 500px;
                                                          background: radial-gradient(circle, var(--primary-glow) 0%, rgba(11,15,25,0) 70%);
                                                          top: 50%;
                                                          left: 50%;
                                                          transform: translate(-50%, -50%);
                                                          z-index: 0;
                                                          pointer-events: none;
                                                      }
                
                                                      /* BỐ CỤC MỚI: BENTO GRID KHÔNG CARD TRUYỀN THỐNG */
                                                      .bento-container {
                                                          position: relative;
                                                          z-index: 1;
                                                          max-width: 650px;
                                                          width: 90%;
                                                          display: grid;
                                                          grid-template-columns: 1fr 1fr;
                                                          gap: 16px;
                                                          animation: fadeIn 0.8s cubic-bezier(0.16, 1, 0.3, 1);
                                                      }
                
                                                      /* Khối tiêu đề chiếm trọn hàng đầu */
                                                      .bento-header {
                                                          grid-column: span 2;
                                                          text-align: center;
                                                          padding: 20px 0;
                                                      }
                
                                                      /* Biểu tượng trạng thái thiết kế mượt mà */
                                                      .status-badge {
                                                          display: inline-flex;
                                                          align-items: center;
                                                          gap: 8px;
                                                          background: rgba(16, 185, 129, 0.1);
                                                          border: 1px solid rgba(16, 185, 129, 0.2);
                                                          color: var(--primary);
                                                          padding: 6px 16px;
                                                          border-radius: 100px;
                                                          font-size: 14px;
                                                          font-weight: 600;
                                                          margin-bottom: 20px;
                                                      }
                
                                                      .status-badge span {
                                                          width: 8px;
                                                          height: 8px;
                                                          background-color: var(--primary);
                                                          border-radius: 50%;
                                                          display: inline-block;
                                                          box-shadow: 0 0 10px var(--primary);
                                                      }
                
                                                      h1 {
                                                          font-size: 38px;
                                                          font-weight: 800;
                                                          margin: 0 0 16px 0;
                                                          letter-spacing: -1px;
                                                          line-height: 1.2;
                                                      }
                
                                                      p {
                                                          color: #94a3b8;
                                                          font-size: 16px;
                                                          line-height: 1.6;
                                                          margin: 0 auto;
                                                          max-width: 500px;
                                                      }
                
                                                      /* Khối nội dung bên trái: Nút bấm lớn tối giản */
                                                      .bento-action {
                                                          grid-column: span 1;
                                                          background: rgba(255, 255, 255, 0.03);
                                                          border: 1px solid rgba(255, 255, 255, 0.05);
                                                          border-radius: 20px;
                                                          padding: 30px;
                                                          display: flex;
                                                          flex-direction: column;
                                                          justify-content: center;
                                                          align-items: flex-start;
                                                          backdrop-filter: blur(10px);
                                                      }
                
                                                      .action-label {
                                                          font-size: 13px;
                                                          text-transform: uppercase;
                                                          letter-spacing: 1px;
                                                          color: var(--text-muted);
                                                          margin-bottom: 16px;
                                                          font-weight: 600;
                                                      }
                
                                                      .btn {
                                                          display: inline-flex;
                                                          align-items: center;
                                                          justify-content: center;
                                                          gap: 8px;
                                                          background-color: #ffffff;
                                                          color: var(--bg-color);
                                                          text-decoration: none;
                                                          padding: 14px 24px;
                                                          border-radius: 12px;
                                                          font-weight: 700;
                                                          font-size: 15px;
                                                          transition: all 0.3s ease;
                                                          width: 100%;
                                                          box-sizing: border-box;
                                                      }
                
                                                      .btn:hover {
                                                          background-color: var(--primary);
                                                          color: #ffffff;
                                                          box-shadow: 0 10px 25px rgba(16, 185, 129, 0.4);
                                                          transform: translateY(-2px);
                                                      }
                
                                                      /* Khối đồ họa bên phải: Trực quan hóa hành động kế tiếp */
                                                      .bento-info {
                                                          grid-column: span 1;
                                                          background: rgba(255, 255, 255, 0.03);
                                                          border: 1px solid rgba(255, 255, 255, 0.05);
                                                          border-radius: 20px;
                                                          padding: 30px;
                                                          display: flex;
                                                          flex-direction: column;
                                                          justify-content: center;
                                                          backdrop-filter: blur(10px);
                                                      }
                
                                                      .info-item {
                                                          display: flex;
                                                          align-items: center;
                                                          gap: 12px;
                                                          color: #94a3b8;
                                                          font-size: 14px;
                                                      }
                
                                                      .info-item svg {
                                                          color: var(--primary);
                                                          flex-shrink: 0;
                                                      }
                
                                                      /* Hiệu ứng mượt mà khi tải trang */
                                                      @keyframes fadeIn {
                                                          from { opacity: 0; transform: translateY(20px); }
                                                          to { opacity: 1; transform: translateY(0); }
                                                      }
                
                                                      /* Responsive cho thiết bị di động */
                                                      @media (max-width: 600px) {
                                                          .bento-container {
                                                              grid-template-columns: 1fr;
                                                              gap: 12px;
                                                          }
                                                          .bento-header, .bento-action, .bento-info {
                                                              grid-column: span 1;
                                                          }
                                                          h1 { font-size: 28px; }
                                                          .bento-action { align-items: center; text-align: center; }
                                                      }
                                                  </style>
                                              </head>
                                              <body>
                
                                                  <div class="bento-container">
                                                      <div class="bento-header">
                                                          <div class="status-badge">
                                                              <span></span> Hệ thống xác thực
                                                          </div>
                                                          <h1>Tài khoản đã sẵn sàng!</h1>
                                                          <p>Quy trình kích hoạt kết thúc hoàn toàn bảo mật. Không gian làm việc và mọi tính năng độc quyền đã mở khóa dành riêng cho bạn.</p>
                                                      </div>
                
                                                      <div class="bento-action">
                                                          <span class="action-label">Hành động</span>
                                                          <a href="http://localhost:3000/html/index.html" class="btn">
                                                              Quay về trang chủ
                                                              <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><line x1="5" y1="12" x2="19" y2="12"></line><polyline points="12 5 19 12 12 19"></polyline></svg>
                                                          </a>
                                                      </div>
                
                                                      <div class="bento-info">
                                                          <div class="info-item">
                                                              <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"></path><polyline points="22 4 12 14.01 9 11.01"></polyline></svg>
                                                              <span>Bạn có thể an toàn đóng tab này hoặc nhấn nút để tiếp tục hành trình.</span>
                                                          </div>
                                                      </div>
                                                  </div>
                
                                              </body>
                                              </html>
    """;
    }
}
