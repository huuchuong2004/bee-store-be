package vn.huuchuong.be_bee_store.auth_module.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import vn.huuchuong.be_bee_store.auth_module.payload.request.*;
import vn.huuchuong.be_bee_store.base.BaseResponse;
import vn.huuchuong.be_bee_store.auth_module.payload.response.AuthResponse;
import vn.huuchuong.be_bee_store.auth_module.payload.response.CreateUserResponse;

import java.util.UUID;

public interface AuthService {
    BaseResponse<AuthResponse> login(@Valid UserLoginResquest request, HttpServletRequest httpReq);

    BaseResponse<CreateUserResponse> register(@Valid CreateUserRequest request);

    BaseResponse<AuthResponse> refresh(@Valid RefreshTokenRequest request, HttpServletRequest httpReq);

    BaseResponse<String> logout(Authentication authentication);

    String activateAccount(UUID accountId);

    BaseResponse<String> resendActivationEmail(String email);


    BaseResponse<String> resetPassword(@Valid ResetPasswordRequest request, HttpServletRequest httpReq);

    BaseResponse<String> sendMailResetPassword(@Valid EmailResetPassword request);
}
