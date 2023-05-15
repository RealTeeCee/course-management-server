package com.aptech.coursemanagementserver.controllers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.aptech.coursemanagementserver.dtos.CourseDto;
import com.aptech.coursemanagementserver.dtos.baseDto.BaseDto;
import com.aptech.coursemanagementserver.enums.AntType;
import com.aptech.coursemanagementserver.models.Course;
import com.aptech.coursemanagementserver.services.CourseService;
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
    public ResponseEntity<List<Course>> getCourses() {
        return ResponseEntity.ok(courseService.findAll());
    }

    @GetMapping(path = "/course/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable("id") long courseId) {
        return ResponseEntity.ok(courseService.findById(courseId));
    }

    @PostMapping(path = "/course/create", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<BaseDto> create(@RequestPart("courseJson") String courseJson,
            @RequestPart("file") MultipartFile file) throws JsonMappingException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        CourseDto courseDto = objectMapper.readValue(courseJson, CourseDto.class);

        try {
            courseDto.setImage(file.getOriginalFilename());
            Course savedCourse = courseService.save(courseDto);

            Path root = Paths.get("assets/course/images/" + savedCourse.getId());
            Files.createDirectories(root);
            System.out.println(file.getOriginalFilename());
            Files.copy(file.getInputStream(), root.resolve(file.getOriginalFilename()),
                    StandardCopyOption.REPLACE_EXISTING);

            return new ResponseEntity<BaseDto>(
                    BaseDto.builder().type(AntType.success).message("Create course successfully").build(),
                    HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<BaseDto>(
                    BaseDto.builder().type(AntType.error).message("Create course Failed: " + e.getMessage()).build(),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path = "/course/download")
    @PreAuthorize("permitAll()")
    public ResponseEntity<Resource> download(@RequestParam String fileName, @RequestParam String courseId)
            throws MalformedURLException {
        // Auto add slash
        Path root = Paths.get("assets", "course", "images", courseId, fileName);
        Resource file = new UrlResource(root.toUri());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(file);
    }

    // @PutMapping
    // public String put() {
    // return "PUT:: management controller";
    // }

    // @DeleteMapping
    // public String delete() {
    // return "DELETE:: management controller";
    // }
}
