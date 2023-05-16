package com.aptech.coursemanagementserver.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aptech.coursemanagementserver.dtos.LessonDto;
import com.aptech.coursemanagementserver.dtos.baseDto.BaseDto;
import com.aptech.coursemanagementserver.enums.AntType;
import com.aptech.coursemanagementserver.services.LessonService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

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
        return new ResponseEntity<List<LessonDto>>(lessonService.findAllBySectionId(sectionId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<BaseDto> createLessonsBySectionId(
            @PathVariable("sectionId") long sectionId,
            @RequestBody LessonDto lessonDto) throws JsonMappingException, JsonProcessingException {
        try {
            return ResponseEntity.ok(lessonService.saveLessonToSection(lessonDto, sectionId));
        } catch (Exception e) {
            log.error("Caused: " + e.getCause() + " ,Message: " + e.getMessage(), e);
            return new ResponseEntity<BaseDto>(BaseDto.builder().type(AntType.error)
                    .message("Failed! Please check your infomation and try again.")
                    .build(),
                    HttpStatus.BAD_REQUEST);
        }

    }
}
