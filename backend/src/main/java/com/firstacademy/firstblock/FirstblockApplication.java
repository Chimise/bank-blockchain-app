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
			Role admin = roleRepo.findByRole(UserRoles.ADMIN);
			if (admin == null) {
				admin = roleRepo.save(new Role().setRole(UserRoles.ADMIN));
			}

			Role userRole = roleRepo.findByRole(UserRoles.USER);

			if (userRole == null) {
				userRole = roleRepo.save(new Role().setRole(UserRoles.USER));
			}

			User user1 = userRepo.findByEmail("chimisepro@gmail.com");
			if (user1 == null) {
				user1 = userRepo.save(new User()
						.setEmail("chimisepro@gmail.com")
						.setPassword(passwordEncoder.encode("password123"))
						.setRoles(Arrays.asList(admin))
						.setFirstName("Chisom").setLastName("Promise"))
						// .setDateOfBirth("05.06.1996")
						.setCountry("Nigeria")
						.setCity("Lagos")
						.setPhoneNumber("08198456723")
						.setUsername("chimisepro")
						.setPermanentAddress("10, Victorial Island")
						.setPresentAddress("10, Victorial Island")
						.setPostalCode("340271");
			}

			User user2 = userRepo.findByEmail("ngooziokafor2@gmail.com");
			if (user2 == null) {
				userRepo.save(new User()
						.setEmail("ngooziokafor2@gmail.com")
						.setPassword(passwordEncoder.encode("password123"))
						.setRoles(Arrays.asList(userRole))
						.setFirstName("Ngozi").setLastName("Okafor")
						.setCountry("Nigeria")
						.setCity("Abuja")
						.setPhoneNumber("08198456723")
						.setUsername("ngooziokafor2")
						.setPermanentAddress("47, Abuja Street")
						.setPresentAddress("47, Abuja Street")
						.setPostalCode("567893"));
			}

			User user3 = userRepo.findByEmail("abdullahi.ibrahim5@gmail.com");

			if (user3 == null) {
				userRepo.save(new User()
						.setEmail("abdullahi.ibrahim5@gmail.com")
						.setPassword(passwordEncoder.encode("password123"))
						.setRoles(Arrays.asList(userRole))
						.setFirstName("Abdullahi").setLastName("Ibrahim")
						.setCountry("Nigeria")
						.setCity("Port Harcourt")
						.setPhoneNumber("08198456723")
						.setUsername("abdullahiibrahim5")
						.setPermanentAddress("32, Port Harcourt Street")
						.setPresentAddress("32, Port Harcourt Street")
						.setPostalCode("440271"));
			}

			User user4 = userRepo.findByEmail("folake.adeleke6@gmail.com");

			if (user4 == null) {
				user4 = userRepo.save(new User()
						.setEmail("folake.adeleke6@gmail.com")
						.setPassword(passwordEncoder.encode("password123"))
						.setRoles(Arrays.asList(userRole))
						.setFirstName("Folake").setLastName("Adeleke")
						.setCountry("Nigeria")
						.setCity("Enugu")
						.setPhoneNumber("08198456723")
						.setUsername("folakeadeleke6")
						.setPermanentAddress("18, Enugu Avenue")
						.setPresentAddress("18, Enugu Avenue")
						.setPostalCode("540271"));
			}

			User user5 = userRepo.findByEmail("yusuf.okonkwo7@gmail.com");
			if (user5 == null) {
				userRepo.save(new User()
						.setEmail("yusuf.okonkwo7@gmail.com")
						.setPassword(passwordEncoder.encode("password123"))
						.setRoles(Arrays.asList(userRole))
						.setFirstName("Yusuf").setLastName("Okonkwo")
						.setCountry("Nigeria")
						.setCity("Kaduna")
						.setPhoneNumber("08198456723")
						.setUsername("yusufokonkwo7")
						.setPermanentAddress("29, Kaduna Lane")
						.setPresentAddress("29, Kaduna Lane")
						.setPostalCode("640271"));
			}

			User user6 = userRepo.findByEmail("adebayo.abubakar9@gmail.com");
			if (user6 == null) {
				userRepo.save(new User()
						.setEmail("adebayo.abubakar9@gmail.com")
						.setPassword(passwordEncoder.encode("password123"))
						.setRoles(Arrays.asList(userRole))
						.setFirstName("Adebayo").setLastName("Abubakar")
						.setCountry("Nigeria")
						.setCity("Benin City")
						.setPhoneNumber("08198456723")
						.setUsername("adebayoabubakar9")
						.setPermanentAddress("12, Benin City Road")
						.setPresentAddress("12, Benin City Road")
						.setPostalCode("850271"));
			}

			User user7 = userRepo.findByEmail("zainab.olawale10@gmail.com");
			if (user7 == null) {
				userRepo.save(new User()
						.setEmail("zainab.olawale10@gmail.com")
						.setPassword(passwordEncoder.encode("password123"))
						.setRoles(Arrays.asList(userRole))
						.setFirstName("Zainab").setLastName("Olawale")
						.setCountry("Nigeria")
						.setCity("Jos")
						.setPhoneNumber("08198456723")
						.setUsername("zainabolawale10")
						.setPermanentAddress("8, Jos Lane")
						.setPresentAddress("8, Jos Lane")
						.setPostalCode("340234"));
			}

			boolean account1Exists = blockchainService.accountExists("3000000000");
			if (!account1Exists) {
				blockchainService.createAccount(user1.getId(), "3000000000", user1.getName(), 5000000);
			}

			boolean account2Exists = blockchainService.accountExists("3100000000");

			if (!account2Exists) {
				blockchainService.createAccount(user2.getId(), "3100000000", user2.getName(), 2000000);
			}

			boolean account3Exists = blockchainService.accountExists("3200000000");
			if (!account3Exists) {
				blockchainService.createAccount(user3.getId(), "3200000000", user3.getName(), 3000000);
			}

			try {
				blockchainService.initLedger();
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
		};
	}
}
