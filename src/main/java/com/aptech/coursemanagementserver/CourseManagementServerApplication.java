package com.aptech.coursemanagementserver;

import static com.aptech.coursemanagementserver.enums.Role.ADMIN;
import static com.aptech.coursemanagementserver.enums.Role.EMPLOYEE;
import static com.aptech.coursemanagementserver.enums.Role.MANAGER;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import com.aptech.coursemanagementserver.configs.ApplicationProperties;
import com.aptech.coursemanagementserver.dtos.CategoryDto;
import com.aptech.coursemanagementserver.dtos.PostDto;
import com.aptech.coursemanagementserver.dtos.RegisterRequestDto;
import com.aptech.coursemanagementserver.models.User;
import com.aptech.coursemanagementserver.services.CategoryService;
import com.aptech.coursemanagementserver.services.CourseService;
import com.aptech.coursemanagementserver.services.PostService;
import com.aptech.coursemanagementserver.services.authServices.AuthenticationService;

@SpringBootApplication
@EnableWebSecurity
@EnableConfigurationProperties(ApplicationProperties.class)
public class CourseManagementServerApplication {

	@Value("${spring.jpa.hibernate.ddl-auto}")
	String seedData;

	public static void main(String[] args) {
		SpringApplication.run(CourseManagementServerApplication.class, args);
	}

	// Define @Bean to tell SpringBoot it should create an instance of the class and
	// register it with the application context. When the application starts up,
	// Spring Boot will call the run() method on each CommandLineRunner Bean in the
	// order in which they were defined.
	@Bean
	CommandLineRunner commandLineRunner(
			AuthenticationService service, CategoryService categoryService, CourseService courseService,
			PostService postService) {
		return args -> {

			if (seedData.equalsIgnoreCase("create")) {

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

				var userTestDto = RegisterRequestDto.builder()
						.first_name("UserTest")
						.last_name("UserTest")
						.email("user-test@mail.com")
						.password("password")
						// .role(USER)
						.isVerified(true)

						.build();
				User userTest = service.register(userTestDto);
				System.out.println(
						"User1 token: " +
								service.generateTokenWithoutVerify(userTest).getAccessToken());
				var userTest2Dto = RegisterRequestDto.builder()
						.first_name("UserTest2")
						.last_name("UserTest2")
						.email("user-test2@mail.com")
						.password("password")
						// .role(USER)
						.isVerified(true)

						.build();
				User userTest2 = service.register(userTest2Dto);
				System.out.println(
						"User2 token: " +
								service.generateTokenWithoutVerify(userTest2).getAccessToken());
				var userTest3Dto = RegisterRequestDto.builder()
						.first_name("UserTest3")
						.last_name("UserTest3")
						.email("user-test3@mail.com")
						.password("password")
						// .role(USER)
						.isVerified(true)

						.build();
				User userTest3 = service.register(userTest3Dto);
				System.out.println(
						"User3 token: " +
								service.generateTokenWithoutVerify(userTest3).getAccessToken());
				postService.create(
						PostDto.builder().content("first post, this is test!").userId(userTest.getId()).build());
				postService.create(PostDto.builder()
						.content("Post2 Lorem ipsum dolor sit amet, consectetur adipiscing elit.")
						.userId(userTest2.getId()).build());
				postService.create(PostDto.builder()
						.content("Post3 Lorem ipsum dolor sit amet, consectetur adipiscing elit.")
						.userId(userTest3.getId()).build());

				List<CategoryDto> categoryDtos = new ArrayList<>();
				CategoryDto category1 = CategoryDto.builder().name("Programming").build();
				CategoryDto category2 = CategoryDto.builder().name("Graphic Design").build();
				CategoryDto category3 = CategoryDto.builder().name("Data Science").build();
				CategoryDto category4 = CategoryDto.builder().name("Artificial Intelligence").build();

				categoryDtos.add(category1);
				categoryDtos.add(category2);
				categoryDtos.add(category3);
				categoryDtos.add(category4);

				categoryService.saveAll(categoryDtos);

				// Category savedCategory1 = categoryService.findById(1);
				// CourseDto course1 = CourseDto.builder().achievementName("Master Java,Master
				// SpringBoot")
				// .image("https://i.ibb.co/0jCVHrQ/spring-boot.png")
				// .duration(300)
				// .description("Description").price(15).net_price(10)
				// .sections(Arrays.asList("Section 1", "Section 2", "Section 3"))
				// .tagName("java,spring boot,hibernate").name("Java SpringBoot 2023")
				// .rating(0)
				// .category(savedCategory1.getId())
				// .build();
				// courseService.save(course1);
			}
		};
	}

	@Override
	public String toString() {
		return "CourseManagementServerApplication []";
	}
}
