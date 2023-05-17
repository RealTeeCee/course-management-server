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

import com.aptech.coursemanagementserver.dtos.SectionDto;
import com.aptech.coursemanagementserver.dtos.baseDto.BaseDto;
import com.aptech.coursemanagementserver.enums.AntType;
import com.aptech.coursemanagementserver.exceptions.BadRequestException;
import com.aptech.coursemanagementserver.exceptions.ResourceNotFoundException;
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
        try {
            return new ResponseEntity<SectionDto>(sectionService.findAllNameByCourseId(courseId), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            throw new ResourceNotFoundException("Sections", "courseId", Long.toString(courseId));
        } catch (Exception e) {
            throw new BadRequestException("Fetch data failed!");
        }

    }

    @PostMapping
    public ResponseEntity<BaseDto> createSectionsByCourseId(@PathVariable("id") long courseId,
            @RequestBody SectionDto sectionDto) throws JsonMappingException, JsonProcessingException {
        try {
            return new ResponseEntity<BaseDto>(sectionService.saveSectionsToCourse(sectionDto, courseId),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<BaseDto>(sectionService.saveSectionsToCourse(sectionDto, courseId),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping
    public ResponseEntity<BaseDto> deleteSection(long sectionId) {
        try {
            return new ResponseEntity<BaseDto>(sectionService.delete(sectionId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<BaseDto>(sectionService.delete(sectionId),
                    HttpStatus.BAD_REQUEST);
        }

    }
}
