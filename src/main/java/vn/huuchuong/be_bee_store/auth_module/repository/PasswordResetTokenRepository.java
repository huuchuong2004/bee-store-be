package vn.huuchuong.be_bee_store.auth_module.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import vn.huuchuong.be_bee_store.auth_module.entity.PasswordResetToken;
import vn.huuchuong.be_bee_store.auth_module.entity.User;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    Optional<PasswordResetToken> findByToken(String token);

    void deleteByUser(User user);
}