package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class UserBaseApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserBaseApplication.class, args);
    }
}
