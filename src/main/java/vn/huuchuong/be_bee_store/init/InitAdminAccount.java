package vn.huuchuong.be_bee_store.init;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import vn.huuchuong.be_bee_store.auth_module.entity.Role;
import vn.huuchuong.be_bee_store.auth_module.entity.User;
import vn.huuchuong.be_bee_store.auth_module.repository.UserRepository;

@Configuration
@RequiredArgsConstructor
public class InitAdminAccount implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        String username = "adminbee";
        String email = "admin@bee-store.vn";
        String rawPassword = "Adm!n1";

        boolean exists = userRepository.existsByUsername(username)
                || userRepository.existsByEmail(email);

        if (!exists) {
            User admin = User.builder()
                    .username(username)
                    .email(email)
                    .password(passwordEncoder.encode(rawPassword))
                    .firstName("Admin")
                    .lastName("BeeStore")
                    .phone("0900000000")
                    .isActive(true)
                    .role(Role.ADMIN)
                    .build();

            userRepository.save(admin);

            System.out.println("Admin account created successfully");
            System.out.println("Username: " + username);
            System.out.println("Password: " + rawPassword);
        } else {
            System.out.println("Admin account already exists");
        }
    }
}