package com.firstacademy.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import com.firstacademy.repository.UserRepository;
import com.firstacademy.entity.User;

@Component
public class DataLoader {

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository) {
        return args -> {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            userRepository.save(new User("testuser1", encoder.encode("testpassword1")));
            userRepository.save(new User("testuser2", encoder.encode("testpassword2")));
        };
    }
}
