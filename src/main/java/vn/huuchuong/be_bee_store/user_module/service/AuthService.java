package vn.huuchuong.be_bee_store.user_module.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import vn.huuchuong.be_bee_store.base.BaseResponse;
import vn.huuchuong.be_bee_store.user_module.payload.request.CreateUserRequest;
import vn.huuchuong.be_bee_store.user_module.payload.request.RefreshTokenRequest;
import vn.huuchuong.be_bee_store.user_module.payload.request.UserLoginResquest;
import vn.huuchuong.be_bee_store.user_module.payload.response.AuthResponse;
import vn.huuchuong.be_bee_store.user_module.payload.response.CreateUserResponse;

import java.util.UUID;

public interface AuthService {
    BaseResponse<AuthResponse> login(@Valid UserLoginResquest request, HttpServletRequest httpReq);

    BaseResponse<CreateUserResponse> register(@Valid CreateUserRequest request);

    BaseResponse<AuthResponse> refresh(@Valid RefreshTokenRequest request, HttpServletRequest httpReq);

    BaseResponse<String> logout(Authentication authentication);

    String activateAccount(UUID accountId);

    BaseResponse<String> resendActivationEmail(String email);
}
