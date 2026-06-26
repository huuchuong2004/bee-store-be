package vn.huuchuong.be_bee_store.user_module.service;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.huuchuong.be_bee_store.auth_module.entity.User;
import vn.huuchuong.be_bee_store.base.BaseResponse;
import vn.huuchuong.be_bee_store.user_module.payload.request.*;
import vn.huuchuong.be_bee_store.user_module.payload.response.LoadUserResponse;
import vn.huuchuong.be_bee_store.user_module.payload.response.UserResponse;

import java.util.List;
import java.util.UUID;

public interface UserService {
    List<User> getUsers(Pageable pageable);

    LoadUserResponse loadUser(String username);
    BaseResponse deleteByUsername(String username);

    BaseResponse updateUser(String username, UpdateUserRequest user);

    BaseResponse setRoleAdmin(@Valid SetAdminRequest req);

    BaseResponse setRoleStaff(@Valid SetStaffRequest req);

    Page<User> search(UserFilterRequest req, Pageable pageable);


    void patch(UUID id, @Valid ProfileUpdateRequest req);


    Integer countUserByRole();
}
