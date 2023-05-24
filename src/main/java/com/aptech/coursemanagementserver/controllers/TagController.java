package com.aptech.coursemanagementserver.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aptech.coursemanagementserver.services.TagService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'EMPLOYEE')")
@Tag(name = "Lesson Endpoints")
@RequestMapping("section/{sectionId}/tag")
@Slf4j
public class TagController {
    private final TagService tagService;
}
