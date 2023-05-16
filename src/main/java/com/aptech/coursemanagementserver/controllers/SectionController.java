package com.aptech.coursemanagementserver.controllers;

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

import com.aptech.coursemanagementserver.dtos.SectionDto;
import com.aptech.coursemanagementserver.dtos.baseDto.BaseDto;
import com.aptech.coursemanagementserver.enums.AntType;
import com.aptech.coursemanagementserver.services.SectionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN')")
@Tag(name = "Section Endpoints")
@RequestMapping("/admin/course/{id}/section")
@Slf4j
public class SectionController {
    private final SectionService sectionService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SectionDto> getSectionsByCourseId(@PathVariable("id") long courseId) {
        return new ResponseEntity<SectionDto>(sectionService.findAllNameByCourseId(courseId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<BaseDto> createSectionsByCourseId(@PathVariable("id") long courseId,
            @RequestBody SectionDto sectionDto) throws JsonMappingException, JsonProcessingException {
        try {
            return ResponseEntity.ok(sectionService.saveSectionsToCourse(sectionDto, courseId));
        } catch (Exception e) {
            log.error("Caused: " + e.getCause() + " ,Message: " + e.getMessage(), e);
            return new ResponseEntity<BaseDto>(BaseDto.builder().type(AntType.error)
                    .message("Failed! Please check your infomation and try again.")
                    .build(),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping
    public ResponseEntity<BaseDto> deleteSection(long sectionId) {
        try {
            return new ResponseEntity<BaseDto>(sectionService.delete(sectionId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<BaseDto>(
                    BaseDto.builder().type(AntType.error)
                            .message("Delete section Failed: " + e.getMessage()).build(),
                    HttpStatus.BAD_REQUEST);
        }

    }
}
