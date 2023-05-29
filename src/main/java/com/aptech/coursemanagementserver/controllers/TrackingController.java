package com.aptech.coursemanagementserver.controllers;

import java.util.NoSuchElementException;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aptech.coursemanagementserver.dtos.LessonTrackingDto;
import com.aptech.coursemanagementserver.dtos.VideoTrackingDto;
import com.aptech.coursemanagementserver.exceptions.BadRequestException;
import com.aptech.coursemanagementserver.exceptions.ResourceNotFoundException;
import com.aptech.coursemanagementserver.services.LessonTrackingService;
import com.aptech.coursemanagementserver.services.VideoTrackingService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/track")
@Tag(name = "CurrentUser Tracking Endpoints")
public class TrackingController {
    private final LessonTrackingService lessonTrackingService;
    private final VideoTrackingService videoTrackingService;

    @PostMapping("/lesson")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MANAGER', 'EMPLOYEE')")
    public ResponseEntity<Boolean> trackLesson(@RequestBody LessonTrackingDto lessonTrackingDto) {
        try {
            return ResponseEntity.ok(lessonTrackingService.trackLesson(lessonTrackingDto));
        } catch (NoSuchElementException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @PostMapping("/video")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MANAGER', 'EMPLOYEE')")
    public ResponseEntity<Boolean> trackVideo(@RequestBody VideoTrackingDto videoTrackingDto) {
        try {
            return ResponseEntity.ok(videoTrackingService.trackVideo(videoTrackingDto));
        } catch (NoSuchElementException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }
}
