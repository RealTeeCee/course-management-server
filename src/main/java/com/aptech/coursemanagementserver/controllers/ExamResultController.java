package com.aptech.coursemanagementserver.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aptech.coursemanagementserver.dtos.ExamResultDto;
import com.aptech.coursemanagementserver.services.ExamResultService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/exam-result")
@Tag(name = "ExamResult Endpoints")
public class ExamResultController {
    private final ExamResultService examResultService;

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MANAGER', 'EMPLOYEE')")
    public ResponseEntity<?> createExamResult(@RequestBody ExamResultDto dto) {

        int examSession = examResultService.createExamResult(dto.getUserId(), dto.getCourseId());
        return ResponseEntity
                .ok(examResultService.findExamResultByCourseIdAndUserIdAndExamSession(dto.getCourseId(),
                        dto.getUserId(),
                        examSession));
    }
}
