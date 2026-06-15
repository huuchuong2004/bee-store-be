package vn.huuchuong.be_bee_store;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing
public class BeBeeStoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(BeBeeStoreApplication.class, args);
    }

}
