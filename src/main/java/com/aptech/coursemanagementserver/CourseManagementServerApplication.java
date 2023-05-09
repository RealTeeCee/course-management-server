package com.aptech.coursemanagementserver;

import static com.aptech.coursemanagementserver.enums.Role.ADMIN;
import static com.aptech.coursemanagementserver.enums.Role.MANAGER;
import static com.aptech.coursemanagementserver.enums.Role.EMPLOYEE;
import static com.aptech.coursemanagementserver.enums.Role.USER;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import com.aptech.coursemanagementserver.dtos.RegisterRequestDto;
import com.aptech.coursemanagementserver.enums.Role;
import com.aptech.coursemanagementserver.services.authServices.AuthenticationService;

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

			System.out.println(
					"Admin token: " + service.generateTokenWithoutVerify(service.register(admin)).getAccessToken());

			var manager = RegisterRequestDto.builder()
					.firstname("manager")
					.lastname("manager")
					.email("manager@mail.com")
					.password("password")
					.role(MANAGER)
					.build();
			System.out.println(
					"Manager token: " + service.generateTokenWithoutVerify(service.register(manager)).getAccessToken());

			var employee = RegisterRequestDto.builder()
					.firstname("employee")
					.lastname("employee")
					.email("employee@mail.com")
					.password("password")
					.role(EMPLOYEE)
					.build();
			System.out.println("Employee token: "
					+ service.generateTokenWithoutVerify(service.register(employee)).getAccessToken());

			var userTest = RegisterRequestDto.builder()
					.firstname("UserTest")
					.lastname("UserTest")
					.email("user-test@mail.com")
					.password("password")
					// .role(USER)
					.build();
			System.out.println(
					"User token: " + service.generateTokenWithoutVerify(service.register(userTest)).getAccessToken());
		};
	}

	@Override
	public String toString() {
		return "CourseManagementServerApplication []";
	}
}
