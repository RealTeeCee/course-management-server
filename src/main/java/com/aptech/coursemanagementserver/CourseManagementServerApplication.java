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
import com.aptech.coursemanagementserver.dtos.AuthorDto;
import com.aptech.coursemanagementserver.dtos.CategoryDto;
import com.aptech.coursemanagementserver.dtos.PostDto;
import com.aptech.coursemanagementserver.dtos.RegisterRequestDto;
import com.aptech.coursemanagementserver.models.User;
import com.aptech.coursemanagementserver.services.AuthorService;
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
			AuthenticationService service,
			CategoryService categoryService,
			AuthorService authorService,
			CourseService courseService,
			PostService postService) {
		return args -> {

			if (seedData.equalsIgnoreCase("create")) {

				var admin = RegisterRequestDto.builder()
						.first_name("ClicknLearn")
						.last_name("")
						.email("admin@mail.com")
						.password("password")
						.imageUrl("https://ibb.co/SKg10dj")
						.role(ADMIN)
						.isVerified(true)
						.build();

				System.out.println(
						"Admin token: " +
								service.generateTokenWithoutVerify(service.register(admin)).getAccessToken());

				var manager = RegisterRequestDto.builder()
						.first_name("Manager")
						.last_name("")
						.email("manager@mail.com")
						.password("password")
						.imageUrl("https://ibb.co/SKg10dj")
						.role(MANAGER)
						.isVerified(true)

						.build();
				System.out.println(
						"Manager token: " +
								service.generateTokenWithoutVerify(service.register(manager)).getAccessToken());

				var employee = RegisterRequestDto.builder()
						.first_name("Employee")
						.last_name("")
						.email("employee@mail.com")
						.password("password")
						.imageUrl("https://ibb.co/SKg10dj")
						.role(EMPLOYEE)
						.isVerified(true)

						.build();
				System.out.println("Employee token: "
						+
						service.generateTokenWithoutVerify(service.register(employee)).getAccessToken());

				var userTestDto = RegisterRequestDto.builder()
						.first_name("TeeCee")
						.last_name("")
						.email("user-test@mail.com")
						.password("password")
						.imageUrl("https://ibb.co/SKg10dj")
						// .role(USER)
						.isVerified(true)

						.build();
				User userTest = service.register(userTestDto);
				System.out.println(
						"User1 token: " +
								service.generateTokenWithoutVerify(userTest).getAccessToken());
				var userTest2Dto = RegisterRequestDto.builder()
						.first_name("Ric")
						.last_name("")
						.email("user-test2@mail.com")
						.password("password")
						.imageUrl("https://ibb.co/SKg10dj")
						// .role(USER)
						.isVerified(true)

						.build();
				User userTest2 = service.register(userTest2Dto);
				System.out.println(
						"User2 token: " +
								service.generateTokenWithoutVerify(userTest2).getAccessToken());
				var userTest3Dto = RegisterRequestDto.builder()
						.first_name("AnPham")
						.last_name("")
						.email("user-test3@mail.com")
						.password("password")
						.imageUrl("https://ibb.co/SKg10dj")
						// .role(USER)
						.isVerified(true)

						.build();
				User userTest3 = service.register(userTest3Dto);
				System.out.println(
						"User3 token: " +
								service.generateTokenWithoutVerify(userTest3).getAccessToken());
				var userTest4Dto = RegisterRequestDto.builder()
						.first_name("DiDi")
						.last_name("")
						.email("user-test4@mail.com")
						.password("password")
						.imageUrl("https://ibb.co/SKg10dj")
						// .role(USER)
						.isVerified(true)

						.build();
				User userTest4 = service.register(userTest4Dto);
				System.out.println(
						"User4 token: " +
								service.generateTokenWithoutVerify(userTest4).getAccessToken());
				var userTest5Dto = RegisterRequestDto.builder()
						.first_name("DucTH")
						.last_name("")
						.email("user-test5@mail.com")
						.password("password")
						.imageUrl("https://ibb.co/SKg10dj")
						// .role(USER)
						.isVerified(true)
						.build();
				User userTest5 = service.register(userTest5Dto);
				System.out.println(
						"User5 token: " +
								service.generateTokenWithoutVerify(userTest5).getAccessToken());
				postService.create(
						PostDto.builder().content("First post, Haha!").userId(userTest.getId()).courseId(1)
								.build());
				postService.create(PostDto.builder()
						.content("Post2 Lorem ipsum dolor sit amet, consectetur adipiscing elit.")
						.userId(userTest2.getId()).courseId(1).build());
				postService.create(PostDto.builder()
						.content("Post3 Lorem ipsum dolor sit amet, consectetur adipiscing elit.")
						.userId(userTest3.getId()).courseId(1).build());

				List<CategoryDto> categoryDtos = new ArrayList<>();
				CategoryDto category1 = CategoryDto.builder().name("Programming").build();
				CategoryDto category2 = CategoryDto.builder().name("Graphic Design").build();
				CategoryDto category3 = CategoryDto.builder().name("Artificial Intelligence").build();
				CategoryDto category4 = CategoryDto.builder().name("Data Science").build();

				categoryDtos.add(category1);
				categoryDtos.add(category2);
				categoryDtos.add(category3);
				categoryDtos.add(category4);

				categoryService.saveAll(categoryDtos);

				List<AuthorDto> authorDtos = new ArrayList<>();
				AuthorDto author1 = AuthorDto.builder().name("Author 1").build();
				AuthorDto author2 = AuthorDto.builder().name("Author 2").build();
				AuthorDto author3 = AuthorDto.builder().name("Author 3").build();
				AuthorDto author4 = AuthorDto.builder().name("Author 4").build();

				authorDtos.add(author1);
				authorDtos.add(author2);
				authorDtos.add(author3);
				authorDtos.add(author4);

				authorService.saveAll(authorDtos);

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
