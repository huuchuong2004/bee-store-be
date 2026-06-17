package vn.huuchuong.be_bee_store.auth_module.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import vn.huuchuong.be_bee_store.auth_module.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {

    Optional<User> findByUsername( String username);

    boolean existsByUsername(String username);

    boolean existsByPhone( String phone);

    boolean existsByEmail( String email);

    Optional<User> findByEmail(String email);

    Optional<User> findByUsernameAndEmail(String username, String email);
}
