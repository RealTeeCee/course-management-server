package com.aptech.coursemanagementserver.controllers;

import static com.aptech.coursemanagementserver.constants.GlobalStorage.DEV_DOMAIN_CLIENT;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aptech.coursemanagementserver.dtos.EnrollmentDto;
import com.aptech.coursemanagementserver.dtos.RatingStarsInterface;
import com.aptech.coursemanagementserver.dtos.UserProfileDto;
import com.aptech.coursemanagementserver.dtos.baseDto.BaseDto;
import com.aptech.coursemanagementserver.enums.AntType;
import com.aptech.coursemanagementserver.exceptions.BadRequestException;
import com.aptech.coursemanagementserver.exceptions.ResourceNotFoundException;
import com.aptech.coursemanagementserver.models.Course;
import com.aptech.coursemanagementserver.repositories.CourseRepository;
import com.aptech.coursemanagementserver.services.EnrollmentService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/enrollment")
@Tag(name = "Enrollment Endpoints")
public class EnrollmentController {
    private final EnrollmentService enrollmentService;
    private final CourseRepository courseRepository;

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MANAGER', 'EMPLOYEE')")
    public ResponseEntity<BaseDto> enroll(@RequestBody EnrollmentDto enrollmentDto) {
        try {
            BaseDto baseDto = enrollmentService.enroll(enrollmentDto);
            Course course = courseRepository.findById(enrollmentDto.getCourse_id()).orElseThrow(
                    () -> new NoSuchElementException(
                            "The course with courseId: [" + enrollmentDto.getCourse_id() + "] is not exist."));
            if (baseDto.getType() == AntType.success) {
                return ResponseEntity.status(HttpStatus.FOUND)
                        .header("Location", DEV_DOMAIN_CLIENT + "/learn/" + course.getSlug())
                        .build();
            } else {
                return ResponseEntity.status(HttpStatus.FOUND)
                        .header("Location", DEV_DOMAIN_CLIENT + "/checkout/" + course.getSlug())
                        .build();
            }
        } catch (NoSuchElementException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @PostMapping("/getEnrollId")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MANAGER', 'EMPLOYEE')")
    public ResponseEntity<Long> getEnroll(@RequestBody EnrollmentDto enrollmentDto) {
        try {
            return ResponseEntity.ok(enrollmentService.getEnrollId(enrollmentDto));
        } catch (NoSuchElementException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @PostMapping("/rating")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MANAGER', 'EMPLOYEE')")
    public ResponseEntity<BaseDto> updateRating(@RequestBody EnrollmentDto enrollmentDto) {
        try {
            return ResponseEntity.ok(enrollmentService.updateRating(enrollmentDto));
        } catch (NoSuchElementException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @GetMapping("/rating/{courseId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MANAGER', 'EMPLOYEE')")
    public ResponseEntity<List<RatingStarsInterface>> getCourseRating(@PathVariable("courseId") long courseId) {
        try {
            return ResponseEntity.ok(enrollmentService.getRatingPercentEachStarsByCourseId(courseId));
        } catch (NoSuchElementException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @PutMapping("/notify")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MANAGER', 'EMPLOYEE')")
    public ResponseEntity<UserProfileDto> updateIsNotify(@RequestBody EnrollmentDto enrollmentDto) {
        try {
            enrollmentService.updateIsNotify(enrollmentDto.isNotify(), enrollmentDto.getUser_id());
            return ResponseEntity.ok().build();
        } catch (NoSuchElementException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }
}
