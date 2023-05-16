package com.aptech.coursemanagementserver.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aptech.coursemanagementserver.dtos.VideoDto;
import com.aptech.coursemanagementserver.dtos.baseDto.BaseDto;
import com.aptech.coursemanagementserver.enums.AntType;
import com.aptech.coursemanagementserver.services.VideoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN')")
@RequestMapping("/admin/lesson/{lessonId}/video")
@Tag(name = "Video Endpoints")
@Slf4j
public class VideoController {
    private final VideoService videoService;

    @PostMapping
    public ResponseEntity<BaseDto> createLessonsBySectionId(
            @PathVariable("lessonId") long lessonId,
            @RequestBody VideoDto videoDto) throws JsonMappingException, JsonProcessingException {
        try {
            return ResponseEntity.ok(videoService.save(videoDto, lessonId));
        } catch (Exception e) {
            log.error("Caused: " + e.getCause() + " ,Message: " + e.getMessage(), e);
            return new ResponseEntity<BaseDto>(BaseDto.builder().type(AntType.error)
                    .message("Failed! Please check your infomation and try again.")
                    .build(),
                    HttpStatus.BAD_REQUEST);
        }

    }
}
