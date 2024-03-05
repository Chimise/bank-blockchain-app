package com.firstacademy.firstblock;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.firstacademy.firstblock.repository.RoleRepository;
import com.firstacademy.firstblock.repository.UserRepository;

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
			PasswordEncoder passwordEncoder) {
		return args -> {
			roleRepo.deleteAll();

			Role admin = roleRepo.save(new Role().setRole(UserRoles.ADMIN));
			Role user = roleRepo.save(new Role().setRole(UserRoles.USER));

			userRepo.deleteAll();
			userRepo.save(new User()
					.setEmail("chimisepro@gmail.com")
					.setPassword(passwordEncoder.encode("password123"))
					.setRoles(Arrays.asList(admin))
					.setFirstName("Chisom").setLastName("Promise"))
					.setCountry("Nigeria")
					.setCity("Lagos")
					.setPhoneNumber("08198456723")
					.setUsername("chimisepro")
					.setPermanentAddress("10, Victorial Island")
					.setPresentAddress("10, Victorial Island");

			userRepo.save(new User()
					.setEmail("ngooziokafor2@gmail.com")
					.setPassword(passwordEncoder.encode("password123"))
					.setRoles(Arrays.asList(user))
					.setFirstName("Ngozi").setLastName("Okafor")
					.setCountry("Nigeria")
					.setCity("Abuja")
					.setPhoneNumber("08198456723")
					.setUsername("ngooziokafor2")
					.setPermanentAddress("47, Abuja Street")
					.setPresentAddress("47, Abuja Street"));

			userRepo.save(new User()
					.setEmail("emeka.yakubu3@gmail.com")
					.setPassword(passwordEncoder.encode("password123"))
					.setRoles(Arrays.asList(user))
					.setFirstName("Emeka").setLastName("Yakubu")
					.setCountry("Nigeria")
					.setCity("Kano")
					.setPhoneNumber("08198456723")
					.setUsername("emekayakubu3")
					.setPermanentAddress("21, Kano Road")
					.setPresentAddress("21, Kano Road"));

			userRepo.save(new User()
					.setEmail("olamide.adewale4@gmail.com")
					.setPassword(passwordEncoder.encode("password123"))
					.setRoles(Arrays.asList(user))
					.setFirstName("Olamide").setLastName("Adewale")
					.setCountry("Nigeria")
					.setCity("Ibadan")
					.setPhoneNumber("08198456723")
					.setUsername("olamideadewale4")
					.setPermanentAddress("15, Ibadan Lane")
					.setPresentAddress("15, Ibadan Lane"));

			userRepo.save(new User()
					.setEmail("abdullahi.ibrahim5@gmail.com")
					.setPassword(passwordEncoder.encode("password123"))
					.setRoles(Arrays.asList(user))
					.setFirstName("Abdullahi").setLastName("Ibrahim")
					.setCountry("Nigeria")
					.setCity("Port Harcourt")
					.setPhoneNumber("08198456723")
					.setUsername("abdullahiibrahim5")
					.setPermanentAddress("32, Port Harcourt Street")
					.setPresentAddress("32, Port Harcourt Street"));

			userRepo.save(new User()
					.setEmail("folake.adeleke6@gmail.com")
					.setPassword(passwordEncoder.encode("password123"))
					.setRoles(Arrays.asList(user))
					.setFirstName("Folake").setLastName("Adeleke")
					.setCountry("Nigeria")
					.setCity("Enugu")
					.setPhoneNumber("08198456723")
					.setUsername("folakeadeleke6")
					.setPermanentAddress("18, Enugu Avenue")
					.setPresentAddress("18, Enugu Avenue"));

			userRepo.save(new User()
					.setEmail("yusuf.okonkwo7@gmail.com")
					.setPassword(passwordEncoder.encode("password123"))
					.setRoles(Arrays.asList(user))
					.setFirstName("Yusuf").setLastName("Okonkwo")
					.setCountry("Nigeria")
					.setCity("Kaduna")
					.setPhoneNumber("08198456723")
					.setUsername("yusufokonkwo7")
					.setPermanentAddress("29, Kaduna Lane")
					.setPresentAddress("29, Kaduna Lane"));

			userRepo.save(new User()
					.setEmail("chinyere.ogundele8@gmail.com")
					.setPassword(passwordEncoder.encode("password123"))
					.setRoles(Arrays.asList(user))
					.setFirstName("Chinyere").setLastName("Ogundele")
					.setCountry("Nigeria")
					.setCity("Calabar")
					.setPhoneNumber("08198456723")
					.setUsername("chinyereogundele8")
					.setPermanentAddress("5, Calabar Street")
					.setPresentAddress("5, Calabar Street"));

			userRepo.save(new User()
					.setEmail("adebayo.abubakar9@gmail.com")
					.setPassword(passwordEncoder.encode("password123"))
					.setRoles(Arrays.asList(user))
					.setFirstName("Adebayo").setLastName("Abubakar")
					.setCountry("Nigeria")
					.setCity("Benin City")
					.setPhoneNumber("08198456723")
					.setUsername("adebayoabubakar9")
					.setPermanentAddress("12, Benin City Road")
					.setPresentAddress("12, Benin City Road"));

			userRepo.save(new User()
					.setEmail("zainab.olawale10@gmail.com")
					.setPassword(passwordEncoder.encode("password123"))
					.setRoles(Arrays.asList(user))
					.setFirstName("Zainab").setLastName("Olawale")
					.setCountry("Nigeria")
					.setCity("Jos")
					.setPhoneNumber("08198456723")
					.setUsername("zainabolawale10")
					.setPermanentAddress("8, Jos Lane")
					.setPresentAddress("8, Jos Lane"));

		};
	}
}
