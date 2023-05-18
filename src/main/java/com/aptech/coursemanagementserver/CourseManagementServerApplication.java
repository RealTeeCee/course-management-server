package com.aptech.coursemanagementserver;

import static com.aptech.coursemanagementserver.enums.Role.ADMIN;
import static com.aptech.coursemanagementserver.enums.Role.EMPLOYEE;
import static com.aptech.coursemanagementserver.enums.Role.MANAGER;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import com.aptech.coursemanagementserver.configs.ApplicationProperties;
import com.aptech.coursemanagementserver.dtos.CategoryDto;
import com.aptech.coursemanagementserver.dtos.CourseDto;
import com.aptech.coursemanagementserver.dtos.RegisterRequestDto;
import com.aptech.coursemanagementserver.models.Category;
import com.aptech.coursemanagementserver.services.CategoryService;
import com.aptech.coursemanagementserver.services.CourseService;
import com.aptech.coursemanagementserver.services.authServices.AuthenticationService;

@SpringBootApplication
@EnableWebSecurity
@EnableConfigurationProperties(ApplicationProperties.class)
public class CourseManagementServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CourseManagementServerApplication.class, args);
	}

	@Bean // Define @Bean to tell SpringBoot it should create an instance of the class and
			// register it with the application context. When the application starts up,
			// Spring Boot will call the run() method on each CommandLineRunner Bean in the
			// order in which they were defined.
	public CommandLineRunner commandLineRunner(
			AuthenticationService service, CategoryService categoryService, CourseService courseService) {
		return args -> {
			var admin = RegisterRequestDto.builder()
					.first_name("Admin")
					.last_name("Admin")
					.email("admin@mail.com")
					.password("password")
					.role(ADMIN)
					.isVerified(true)
					.build();

			System.out.println(
					"Admin token: " +
							service.generateTokenWithoutVerify(service.register(admin)).getAccessToken());

			var manager = RegisterRequestDto.builder()
					.first_name("manager")
					.last_name("manager")
					.email("manager@mail.com")
					.password("password")
					.role(MANAGER)
					.isVerified(true)

					.build();
			System.out.println(
					"Manager token: " +
							service.generateTokenWithoutVerify(service.register(manager)).getAccessToken());

			var employee = RegisterRequestDto.builder()
					.first_name("employee")
					.last_name("employee")
					.email("employee@mail.com")
					.password("password")
					.role(EMPLOYEE)
					.isVerified(true)

					.build();
			System.out.println("Employee token: "
					+
					service.generateTokenWithoutVerify(service.register(employee)).getAccessToken());

			var userTest = RegisterRequestDto.builder()
					.first_name("UserTest")
					.last_name("UserTest")
					.email("user-test@mail.com")
					.password("password")
					// .role(USER)
					.isVerified(true)

					.build();
			System.out.println(
					"User token: " +
							service.generateTokenWithoutVerify(service.register(userTest)).getAccessToken());

			CategoryDto category1 = CategoryDto.builder().name("Programming").build();
			Category savedCategory1 = categoryService.save(category1);

			CourseDto course1 = CourseDto.builder().achievementName("Master Java,Master SpringBoot")
					.duration(300)
					.description("Description").price(15).net_price(10)
					.sections(Arrays.asList("Section 1", "Section 2", "Section 3"))
					.tagName("Java,SpringBoot,Hibernate").name("Java SpringBoot 2023").category(savedCategory1.getId())
					.build();
			courseService.save(course1);
		};
	}

	@Override
	public String toString() {
		return "CourseManagementServerApplication []";
	}
}
