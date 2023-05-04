package com.aptech.coursemanagementserver;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import com.aptech.coursemanagementserver.dtos.RegisterRequestDto;
import com.aptech.coursemanagementserver.services.authServices.AuthenticationService;

import static com.aptech.coursemanagementserver.models.Role.ADMIN;
import static com.aptech.coursemanagementserver.models.Role.MANAGER;

@SpringBootApplication
@EnableWebSecurity

public class CourseManagementServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CourseManagementServerApplication.class, args);
	}

	@Bean // Define @Bean to tell SpringBoot it should create an instance of the class and
			// register it with the application context. When the application starts up,
			// Spring Boot will call the run() method on each CommandLineRunner Bean in the
			// order in which they were defined.
	public CommandLineRunner commandLineRunner(
			AuthenticationService service) {
		return args -> {
			var admin = RegisterRequestDto.builder()
					.firstname("Admin")
					.lastname("Admin")
					.email("admin@mail.com")
					.password("password")
					.role(ADMIN)
					.build();
			System.out.println("Admin token: " + service.register(admin).getAccessToken());

			var manager = RegisterRequestDto.builder()
					.firstname("Admin")
					.lastname("Admin")
					.email("manager@mail.com")
					.password("password")
					.role(MANAGER)
					.build();
			System.out.println("Manager token: " + service.register(manager).getAccessToken());
		};
	}

	@Override
	public String toString() {
		return "CourseManagementServerApplication []";
	}
}
