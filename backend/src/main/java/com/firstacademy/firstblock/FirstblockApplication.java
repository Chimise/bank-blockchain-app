package com.firstacademy.firstblock;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.firstacademy.firstblock.repository.UserRepository;
import com.firstacademy.firstblock.model.User;

@SpringBootApplication
public class FirstblockApplication {

	public static void main(String[] args) {
		SpringApplication.run(FirstblockApplication.class, args);
	}

	@Bean
	public CommandLineRunner dataLoader(UserRepository userRepo, PasswordEncoder passwordEncoder) {
		return args -> {
			userRepo.save(new User().setEmail("chimisepro@gmail.com").setPassword(passwordEncoder.encode("password123"))
					.setFirstName("Chisom").setLastName("Promise"));
			userRepo.save(new User().setEmail("prosom@gmail.com").setPassword(passwordEncoder.encode("password$123"))
					.setFirstName("Nasiru").setLastName("Ibraham"));
		};
	}

}
