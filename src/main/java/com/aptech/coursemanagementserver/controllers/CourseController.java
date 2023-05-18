package com.aptech.coursemanagementserver.controllers;

import static com.aptech.coursemanagementserver.constants.GlobalStorage.BAD_REQUEST_EXCEPTION;
import static com.aptech.coursemanagementserver.constants.GlobalStorage.GLOBAL_EXCEPTION;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.aptech.coursemanagementserver.dtos.CourseDto;
import com.aptech.coursemanagementserver.dtos.baseDto.BaseDto;
import com.aptech.coursemanagementserver.enums.AntType;
import com.aptech.coursemanagementserver.exceptions.BadRequestException;
import com.aptech.coursemanagementserver.exceptions.InvalidTokenException;
import com.aptech.coursemanagementserver.exceptions.ResourceNotFoundException;
import com.aptech.coursemanagementserver.models.Course;
import com.aptech.coursemanagementserver.services.CourseService;
import com.aptech.coursemanagementserver.utils.FileUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
@PreAuthorize("hasAnyRole('ADMIN')")
@Tag(name = "Course Endpoints")
public class CourseController {
        private final CourseService courseService;

        @GetMapping(path = "/courses")
        public ResponseEntity<List<CourseDto>> getCourses() {
                try {
                        List<Course> courses = courseService.findAll();
                        List<CourseDto> courseDtos = new ArrayList<>();
                        for (Course course : courses) {
                                List<String> achievementsList = course.getAchievements().stream()
                                                .map(achievement -> achievement.getName())
                                                .toList();
                                List<String> tagsList = course.getTags().stream().map(tag -> tag.getName()).toList();
                                new CourseDto();
                                CourseDto courseDto = CourseDto.builder().name(course.getName())
                                                .price(course.getPrice())
                                                .net_price(course.getNet_price()).slug(course.getSlug())
                                                .image(course.getImage())
                                                .sections(course.getSections().stream()
                                                                .map(section -> section.getName())
                                                                .toList())
                                                .category(course.getCategory().getId())
                                                .achievementName(String.join(",", achievementsList))
                                                .tagName(String.join(",", tagsList))
                                                .duration(course.getDuration()).build();
                                courseDtos.add(courseDto);
                        }

                        return ResponseEntity.ok(courseDtos);
                } catch (Exception e) {
                        throw new BadRequestException("Fetch data failed!");
                }

        }

        @GetMapping(path = "/course/{id}")
        public ResponseEntity<CourseDto> getCourseById(@PathVariable("id") long id) {
                try {
                        Course course = courseService.findById(id);

                        List<String> achievementsList = course.getAchievements().stream()
                                        .map(achievement -> achievement.getName())
                                        .toList();
                        List<String> tagsList = course.getTags().stream().map(tag -> tag.getName()).toList();
                        CourseDto courseDto = CourseDto.builder().name(course.getName()).price(course.getPrice())
                                        .net_price(course.getNet_price()).slug(course.getSlug())
                                        .image(course.getImage())
                                        .sections(course.getSections().stream().map(section -> section.getName())
                                                        .toList())
                                        .category(course.getCategory().getId())
                                        .achievementName(String.join(",", achievementsList))
                                        .tagName(String.join(",", tagsList))
                                        .duration(course.getDuration()).build();
                        return ResponseEntity.ok(courseDto);
                } catch (Exception e) {
                        throw new ResourceNotFoundException("Course", "CourseId: ", Long.toString(id));
                }

        }

        @PostMapping(path = "/course/create", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
        public ResponseEntity<BaseDto> create(@RequestPart("courseJson") String courseJson,
                        @RequestPart("file") MultipartFile file) throws JsonMappingException, JsonProcessingException {
                ObjectMapper objectMapper = new ObjectMapper();

                try {
                        String extension = FileUtils.getFileExtension(file);

                        CourseDto courseDto = objectMapper.readValue(courseJson, CourseDto.class);
                        courseDto.setImage(courseDto.getName() + "_InDB." + extension);
                        Course savedCourse = courseService.save(courseDto);

                        Path root = Paths.get("assets/images/course");
                        Files.createDirectories(root);

                        Files.copy(file.getInputStream(), root.resolve(Instant.now().atZone(ZoneId.systemDefault())
                                        .format(DateTimeFormatter.ofPattern("ddMMyyyy")) + "_"
                                        + savedCourse.getName().replace(" ", "-")
                                        + "_" + savedCourse.getId() + "." + extension),
                                        StandardCopyOption.REPLACE_EXISTING);

                        return new ResponseEntity<BaseDto>(
                                        BaseDto.builder().type(AntType.success).message("Create course successfully")
                                                        .build(),
                                        HttpStatus.OK);

                } catch (Exception e) {
                        throw new BadRequestException(BAD_REQUEST_EXCEPTION);

                }
        }

        @GetMapping(path = "/course/download")
        @PreAuthorize("permitAll()")
        public ResponseEntity<Resource> download(@RequestParam long courseId)
                        throws MalformedURLException {
                try {
                        Course course = courseService.findById(courseId);
                        String fileExtension = FileUtils.getFileExtension(course.getImage());
                        // Auto add slash
                        Path root = Paths.get("assets", "images", "course",
                                        course.getUpdated_at().atZone(ZoneId.systemDefault())
                                                        .format(DateTimeFormatter.ofPattern("ddMMyyyy")) + "_"
                                                        + course.getName().replace(" ", "-")
                                                        + "_" + course.getId() + "." + fileExtension);

                        Resource file = new UrlResource(root.toUri());

                        return ResponseEntity.ok()
                                        .header(HttpHeaders.CONTENT_DISPOSITION,
                                                        "attachment; filename=\"" + file.getFilename() + "\"")
                                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                                        .body(file);
                } catch (Exception e) {
                        throw new BadRequestException(GLOBAL_EXCEPTION);
                }

        }

        @PutMapping(path = "/course", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
        public ResponseEntity<BaseDto> updateCourse(@RequestPart("courseJson") String courseJson,
                        @RequestPart("file") MultipartFile file, long courseId) {
                ObjectMapper objectMapper = new ObjectMapper();

                try {
                        String extension = FilenameUtils.getExtension(file.getOriginalFilename());

                        CourseDto courseDto = objectMapper.readValue(courseJson, CourseDto.class);
                        courseDto.setImage(courseDto.getName() + "_InDB." + extension);

                        Course course = courseService.findById(courseId);
                        Course savedCourse = courseService.setProperties(courseDto, course);

                        Path root = Paths.get("assets/images/course");
                        Files.createDirectories(root);

                        System.out.println(file.getOriginalFilename());

                        Files.copy(file.getInputStream(), root.resolve(Instant.now().atZone(ZoneId.systemDefault())
                                        .format(DateTimeFormatter.ofPattern("ddMMyyyy")) + "_"
                                        + savedCourse.getName().replace(" ", "-")
                                        + "_" + savedCourse.getId() + "." + extension),
                                        StandardCopyOption.REPLACE_EXISTING);

                        return new ResponseEntity<BaseDto>(
                                        BaseDto.builder().type(AntType.success).message("Create course successfully")
                                                        .build(),
                                        HttpStatus.OK);

                } catch (Exception e) {
                        throw new BadRequestException(BAD_REQUEST_EXCEPTION);
                }
        }

        @DeleteMapping(path = "/course")
        public ResponseEntity<BaseDto> deleteCourse(long courseId) {
                try {
                        Course course = courseService.findById(courseId);
                        String fileExtension = FilenameUtils.getExtension(course.getImage());
                        // Auto add slash
                        Path root = Paths.get("assets", "images", "course",
                                        course.getUpdated_at().atZone(ZoneId.systemDefault())
                                                        .format(DateTimeFormatter.ofPattern("ddMMyyyy")) + "_"
                                                        + course.getName().replace(" ", "-")
                                                        + "_" + course.getId() + "." + fileExtension);
                        Files.delete(root);

                        return new ResponseEntity<BaseDto>(courseService.delete(courseId), HttpStatus.OK);
                } catch (InvalidTokenException e) {
                        return new ResponseEntity<BaseDto>(BaseDto.builder().type(AntType.error)
                                        .message(BAD_REQUEST_EXCEPTION)
                                        .build(), HttpStatus.BAD_REQUEST);
                } catch (Exception e) {
                        return new ResponseEntity<BaseDto>(courseService.delete(courseId),
                                        HttpStatus.BAD_REQUEST);
                }

        }
}
