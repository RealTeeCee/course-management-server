package com.aptech.coursemanagementserver.controllers;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aptech.coursemanagementserver.dtos.EnrollmentDto;
import com.aptech.coursemanagementserver.dtos.baseDto.BaseDto;
import com.aptech.coursemanagementserver.exceptions.BadRequestException;
import com.aptech.coursemanagementserver.exceptions.ResourceNotFoundException;
import com.aptech.coursemanagementserver.services.EnrollmentService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/enrollment")
@Tag(name = "Enrollment Endpoints")
public class EnrollmentController {
    private final EnrollmentService enrollmentService;

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MANAGER', 'EMPLOYEE')")
    public ResponseEntity<BaseDto> enroll(@RequestBody EnrollmentDto enrollmentDto) {
        try {
            return ResponseEntity.ok(enrollmentService.enroll(enrollmentDto));
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
}
