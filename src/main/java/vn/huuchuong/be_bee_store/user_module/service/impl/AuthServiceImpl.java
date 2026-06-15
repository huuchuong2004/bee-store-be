package vn.huuchuong.be_bee_store.user_module.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import vn.huuchuong.be_bee_store.base.BaseResponse;
import vn.huuchuong.be_bee_store.exception.BusinessException;
import vn.huuchuong.be_bee_store.user_module.entity.User;
import vn.huuchuong.be_bee_store.user_module.payload.request.CreateUserRequest;
import vn.huuchuong.be_bee_store.user_module.payload.request.UserLoginResquest;
import vn.huuchuong.be_bee_store.user_module.payload.response.AuthResponse;
import vn.huuchuong.be_bee_store.user_module.payload.response.CreateUserResponse;
import vn.huuchuong.be_bee_store.user_module.payload.response.UserLoginResponse;
import vn.huuchuong.be_bee_store.user_module.repository.RefreshTokenRepository;
import vn.huuchuong.be_bee_store.user_module.repository.UserRepository;
import vn.huuchuong.be_bee_store.user_module.service.AuthService;
import vn.huuchuong.be_bee_store.utils.JwtUtils;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;
    private final ModelMapper modelMapper;
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
//
//        // Tạo link kích hoạt
//        String activationLink = ServletUriComponentsBuilder
//                .fromCurrentContextPath()
//                .path("/api/v1/auth/active/{accountId}")
//                .buildAndExpand(savedUser.getId())
//                .toUriString();
//
//        // Gửi mail bằng hàm chuyên dụng
//        BaseResponse<String> mailResult =
//                mailSenderService.sendActivationEmail(savedUser.getEmail(), activationLink);
//
//        String message;
//        if (mailResult.getData() == null) {
//            message = "Tạo tài khoản thành công nhưng gửi email kích hoạt thất bại: "
//                    + mailResult.getMessage();
//        } else {
//            message = "Tạo tài khoản thành công! Vui lòng kiểm tra email để kích hoạt.";
//        }

            String message = "Tạo tài khoản thành công! Vui lòng kiểm tra email để kích hoạt.";

        return new BaseResponse<>(response, message);
    }
}
