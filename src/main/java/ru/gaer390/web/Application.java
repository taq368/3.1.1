package ru.gaer390.web;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.gaer390.web.role.RoleServiceImpl;
import ru.gaer390.web.role.Role;
import ru.gaer390.web.user.User;
import ru.gaer390.web.user.UserServiceImpl;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashSet;

@SpringBootApplication
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(10);
	}

	@Bean
	CommandLineRunner commandLineRunner(UserServiceImpl userService, RoleServiceImpl roleService) {
		return args -> {
			roleService.addNewRole(new Role(null, "ROLE_USER"));
			roleService.addNewRole(new Role(null, "ROLE_ADMIN"));

			userService.addNewUser(new User(null, "gaer390@yandex.ru", "Kirill", "Kombarov", LocalDate.of(1996, Month.MARCH, 31), "123", true, new HashSet<>()));
			userService.addNewUser(new User(null, "grom@mail.ru", "Stas", "Gromakov", LocalDate.of(1999, Month.APRIL, 14), "123", true, new HashSet<>()));

			userService.addRoleToUser("gaer390@yandex.ru", "ROLE_ADMIN");
			userService.addRoleToUser("grom@mail.ru", "ROLE_USER");
			userService.addRoleToUser("gaer390@yandex.ru", "ROLE_USER");
		};
	}
}
