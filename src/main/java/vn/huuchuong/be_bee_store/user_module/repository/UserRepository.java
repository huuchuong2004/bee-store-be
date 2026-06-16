package vn.huuchuong.be_bee_store.user_module.repository;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import vn.huuchuong.be_bee_store.user_module.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {

    Optional<User> findByUsername( String username);

    boolean existsByUsername(String username);

    boolean existsByPhone( String phone);

    boolean existsByEmail( String email);

    Optional<User> findByEmail(String email);
}
