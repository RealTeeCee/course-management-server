package com.aptech.coursemanagementserver.controllers;

import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aptech.coursemanagementserver.dtos.VideoDto;
import com.aptech.coursemanagementserver.dtos.baseDto.BaseDto;
import com.aptech.coursemanagementserver.enums.AntType;
import com.aptech.coursemanagementserver.exceptions.BadRequestException;
import com.aptech.coursemanagementserver.exceptions.ResourceNotFoundException;
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

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VideoDto> getVideoByLessonId(
            @PathVariable("lessonId") long lessonId) {
        try {
            return new ResponseEntity<VideoDto>(videoService.findByLessonId(lessonId), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            throw new ResourceNotFoundException("Video", "videoId", Long.toString(lessonId));
        } catch (Exception e) {
            throw new BadRequestException("Fetch data failed!");

        }
    }

    @PostMapping
    public ResponseEntity<BaseDto> createLessonsBySectionId(
            @PathVariable("lessonId") long lessonId,
            @RequestBody VideoDto videoDto) throws JsonMappingException, JsonProcessingException {
        try {
            return new ResponseEntity<BaseDto>(videoService.save(videoDto, lessonId), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Caused: " + e.getCause() + " ,Message: " + e.getMessage(), e);
            return new ResponseEntity<BaseDto>(videoService.save(videoDto, lessonId), HttpStatus.BAD_REQUEST);

        }
    }

    @DeleteMapping
    public ResponseEntity<BaseDto> deleteVideo(long videoId) {
        try {
            return new ResponseEntity<BaseDto>(videoService.delete(videoId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<BaseDto>(videoService.delete(videoId),
                    HttpStatus.BAD_REQUEST);
        }

    }
}
