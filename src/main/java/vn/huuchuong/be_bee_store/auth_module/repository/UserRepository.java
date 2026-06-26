package vn.huuchuong.be_bee_store.auth_module.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import vn.huuchuong.be_bee_store.auth_module.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {

    Optional<User> findByUsername(String username);

    // 🔹 Kiểm tra username đã tồn tại (cho register hoặc validate)
    boolean existsByUsername(String username);

    // 🔹 Kiểm tra email đã tồn tại (nếu có field email)
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);


    boolean existsByPhone(String phone);


    @Query("SELECT COUNT(u) FROM User u WHERE u.role = 'USER'")
    Integer countUserByRole();
    Optional<User> findByUsernameAndEmail(String username, String email);
}
