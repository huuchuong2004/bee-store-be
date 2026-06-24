package vn.huuchuong.be_bee_store.user_module.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.huuchuong.be_bee_store.user_module.payload.response.UserResponse;

import java.util.UUID;

public interface UserService {
    Page<UserResponse> getAllUsers(Pageable pageable);

    void deleteUserById(UUID id);
}
