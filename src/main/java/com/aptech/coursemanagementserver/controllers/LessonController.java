package com.aptech.coursemanagementserver.controllers;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aptech.coursemanagementserver.dtos.LessonDto;
import com.aptech.coursemanagementserver.dtos.baseDto.BaseDto;
import com.aptech.coursemanagementserver.exceptions.BadRequestException;
import com.aptech.coursemanagementserver.exceptions.ResourceNotFoundException;
import com.aptech.coursemanagementserver.services.LessonService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import static com.aptech.coursemanagementserver.constants.GlobalStorage.FETCHING_FAILED;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN')")
@Tag(name = "Lesson Endpoints")
@RequestMapping("/admin/section/{sectionId}/lesson")
@Slf4j
public class LessonController {
    private final LessonService lessonService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<LessonDto>> getLessonsBySectionId(
            @PathVariable("sectionId") long sectionId) {
        try {
            return new ResponseEntity<List<LessonDto>>(lessonService.findAllBySectionId(sectionId), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            throw new ResourceNotFoundException("Lessons", "sectionId", Long.toString(sectionId));
        } catch (Exception e) {
            throw new BadRequestException(FETCHING_FAILED);
        }
    }

    @PostMapping
    public ResponseEntity<BaseDto> createLessonsBySectionId(
            @PathVariable("sectionId") long sectionId,
            @RequestBody LessonDto lessonDto) throws JsonMappingException, JsonProcessingException {
        try {
            return new ResponseEntity<BaseDto>(lessonService.saveLessonsToSection(lessonDto, sectionId), HttpStatus.OK);

        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        } catch (Exception e) {
            log.error("Caused: " + e.getCause() + " ,Message: " + e.getMessage(), e);
            throw new BadRequestException(e.getMessage());
        }
    }

    @PutMapping
    public ResponseEntity<BaseDto> createLessonsBySectionId(
            @RequestBody LessonDto lessonDto, long lessonId) throws JsonMappingException, JsonProcessingException {
        try {
            return new ResponseEntity<BaseDto>(lessonService.updateLesson(lessonDto, lessonId), HttpStatus.OK);

        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @DeleteMapping
    public ResponseEntity<BaseDto> deleteLesson(long lessonId) {
        try {
            return new ResponseEntity<BaseDto>(lessonService.delete(lessonId), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }

    }
}
