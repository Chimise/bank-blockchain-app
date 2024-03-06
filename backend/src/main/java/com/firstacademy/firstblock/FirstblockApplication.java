package com.firstacademy.firstblock;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.firstacademy.firstblock.repository.RoleRepository;
import com.firstacademy.firstblock.repository.UserRepository;
import com.firstacademy.firstblock.service.BlockchainService;
import com.firstacademy.firstblock.model.Role;
import com.firstacademy.firstblock.model.User;
import com.firstacademy.firstblock.model.UserRoles;

@SpringBootApplication
public class FirstblockApplication {

	public static void main(String[] args) {
		SpringApplication.run(FirstblockApplication.class, args);
	}

	@Bean
	public CommandLineRunner dataLoader(UserRepository userRepo, RoleRepository roleRepo,
			PasswordEncoder passwordEncoder, BlockchainService blockchainService) {
		return args -> {
			roleRepo.deleteAll();

			Role admin = roleRepo.save(new Role().setRole(UserRoles.ADMIN));
			Role user = roleRepo.save(new Role().setRole(UserRoles.USER));

			userRepo.deleteAll();
			User user1 = userRepo
					.save(new User().setEmail("chimisepro@gmail.com").setPassword(passwordEncoder.encode("password123"))
							.setRoles(Arrays.asList(admin))
							.setFirstName("Chisom").setLastName("Promise"));
			User user2 = userRepo
					.save(new User().setEmail("prosom@gmail.com").setPassword(passwordEncoder.encode("password$123"))
							.setRoles(Arrays.asList(user))
							.setFirstName("Nasiru").setLastName("Ibraham"));

			try {
				blockchainService.initLedger();
			} catch (Exception e) {
				System.out.print(e);
				// TODO: handle exception
			}

		};
	}
}
