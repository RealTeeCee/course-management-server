package com.aptech.coursemanagementserver.controllers;

import static com.aptech.coursemanagementserver.constants.GlobalStorage.FETCHING_FAILED;

import java.util.NoSuchElementException;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aptech.coursemanagementserver.dtos.LearningDto;
import com.aptech.coursemanagementserver.dtos.LessonTrackingDto;
import com.aptech.coursemanagementserver.exceptions.BadRequestException;
import com.aptech.coursemanagementserver.exceptions.ResourceNotFoundException;
import com.aptech.coursemanagementserver.services.LessonTrackingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/track")
@Tag(name = "Tracking Endpoints")
public class TrackingController {
    private final LessonTrackingService lessonTrackingService;

    @PostMapping(path = "/load")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MANAGER', 'EMPLOYEE')")
    public ResponseEntity<LessonTrackingDto> loadTrack(@RequestBody LessonTrackingDto lessonTrackingDto)
            throws JsonMappingException, JsonProcessingException {
        try {
            return ResponseEntity.ok(lessonTrackingService.loadTrack(lessonTrackingDto));
        } catch (NoSuchElementException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException(FETCHING_FAILED);
        }
    }

    @PostMapping(path = "/save")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MANAGER', 'EMPLOYEE')")
    public ResponseEntity<Boolean> saveTrack(@RequestBody LessonTrackingDto lessonTrackingDto)
            throws JsonMappingException, JsonProcessingException {
        try {
            return ResponseEntity.ok(lessonTrackingService.saveTrack(lessonTrackingDto));
        } catch (NoSuchElementException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @PostMapping(path = "/complete")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MANAGER', 'EMPLOYEE')")
    public ResponseEntity<Double> complete(@RequestBody LessonTrackingDto lessonTrackingDto)
            throws JsonMappingException, JsonProcessingException {
        try {
            return ResponseEntity.ok(lessonTrackingService.complete(lessonTrackingDto));
        } catch (NoSuchElementException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @PostMapping(path = "/load-progress")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MANAGER', 'EMPLOYEE')")
    public ResponseEntity<Double> loadProgress(@RequestBody LessonTrackingDto lessonTrackingDto)
            throws JsonMappingException, JsonProcessingException {
        try {
            return ResponseEntity.ok(lessonTrackingService.loadProgress(lessonTrackingDto.getEnrollmentId(),
                    lessonTrackingDto.getCourseId()));
        } catch (NoSuchElementException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @GetMapping(path = "/learning/{courseId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MANAGER', 'EMPLOYEE')")
    public ResponseEntity<LearningDto> getLearnDetail(@PathVariable("courseId") long courseId)
            throws JsonMappingException, JsonProcessingException {
        try {
            return ResponseEntity.ok(lessonTrackingService.getLearnDetails(courseId));
        } catch (NoSuchElementException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }

}
