package vn.huuchuong.be_bee_store.user_module.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import vn.huuchuong.be_bee_store.base.BaseResponse;
import vn.huuchuong.be_bee_store.user_module.payload.request.CreateUserRequest;
import vn.huuchuong.be_bee_store.user_module.payload.request.RefreshTokenRequest;
import vn.huuchuong.be_bee_store.user_module.payload.request.UserLoginResquest;
import vn.huuchuong.be_bee_store.user_module.payload.response.AuthResponse;
import vn.huuchuong.be_bee_store.user_module.payload.response.CreateUserResponse;
import vn.huuchuong.be_bee_store.user_module.service.AuthService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Schema(name = "AuthController", description = "API cho các chức năng liên quan đến xác thực và quản lý tài khoản người dùng như đăng nhập, đăng ký, kích hoạt tài khoản, v.v.")
public class AuthController {


    private final AuthService authService;

    @Operation(summary = "Đăng nhập", description = "Đăng nhập và trả về access + refresh token")
    @Transactional
    @PostMapping("/login")
    public ResponseEntity<BaseResponse<AuthResponse>> login(
            @Valid @RequestBody UserLoginResquest request,
            HttpServletRequest httpReq) {

        BaseResponse<AuthResponse> response = authService.login(request, httpReq);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    @Operation(
            summary = "Refresh Token",
            description = "API này sẽ được gọi khi Access token hết hạn"
    )
    public ResponseEntity<BaseResponse<AuthResponse>> refresh(
            @Valid @RequestBody RefreshTokenRequest request,
            HttpServletRequest httpReq) {

        BaseResponse<AuthResponse> response = authService.refresh(request, httpReq);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    @Operation(
            summary = "Logout",
            description = "API này sẽ được gọi khi user cần đăng xuất, nó sẽ xóa token ra khỏi DB"
    )
    public ResponseEntity<BaseResponse<String>> logout(Authentication authentication) {
        BaseResponse<String> response = authService.logout(authentication);
        return ResponseEntity.ok(response);
    }

    @Transactional
    @PostMapping("/register")
    @Operation(
            summary = "Đăng Kí",
            description = "API Đắng Ki để tạo tài khoản mới, nhận thông tin người dùng và trả về thông tin tài khoản đã được tạo thành công"
    )
    public ResponseEntity<BaseResponse<CreateUserResponse>> register(
            @RequestBody @Valid CreateUserRequest request) {

        BaseResponse<CreateUserResponse> response = authService.register(request);
        return ResponseEntity.status(201).body(response);
    }



    @GetMapping(value = "/active/{accountId}", produces = MediaType.TEXT_HTML_VALUE)
    @Operation(
            summary = "Kích Hoạt    ",
            description = "API Kích Hoạt Tài Khoản ( FE không gọi API Này"
    )
    public ResponseEntity<String> activate(@PathVariable("accountId") UUID accountId) {
        String html = authService.activateAccount(accountId);
        return ResponseEntity.ok(html);
    }

    @PostMapping("/resend-activation")
    @Operation(
            summary = "Kích Hoạt Tài Khoản Lại Khi Người Dùng Chưa Nhận Được Mail, Người Dùng Sẽ Chủ Động Gọi function này    ",
            description = "API Kích Hoạt Tài Khoản "
    )
    public ResponseEntity<BaseResponse<String>> resendActivation(@RequestParam String email) {
        BaseResponse<String> response = authService.resendActivationEmail(email);
        return ResponseEntity.ok(response);
    }
}
