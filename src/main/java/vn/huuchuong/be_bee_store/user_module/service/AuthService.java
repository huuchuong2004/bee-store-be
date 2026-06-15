package vn.huuchuong.be_bee_store.user_module.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import vn.huuchuong.be_bee_store.base.BaseResponse;
import vn.huuchuong.be_bee_store.user_module.payload.request.CreateUserRequest;
import vn.huuchuong.be_bee_store.user_module.payload.request.UserLoginResquest;
import vn.huuchuong.be_bee_store.user_module.payload.response.AuthResponse;
import vn.huuchuong.be_bee_store.user_module.payload.response.CreateUserResponse;

public interface AuthService {
    BaseResponse<AuthResponse> login(@Valid UserLoginResquest request, HttpServletRequest httpReq);

    BaseResponse<CreateUserResponse> register(@Valid CreateUserRequest request);
}
