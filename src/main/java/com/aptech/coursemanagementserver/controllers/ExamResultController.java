package com.aptech.coursemanagementserver.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aptech.coursemanagementserver.dtos.AccomplishmentsDto;
import com.aptech.coursemanagementserver.dtos.ExamResultDto;
import com.aptech.coursemanagementserver.dtos.FinishExamRequestDto;
import com.aptech.coursemanagementserver.dtos.FinishExamResponseDto;
import com.aptech.coursemanagementserver.dtos.RetakeExamDto;
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

    @PostMapping("/finish-exam")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MANAGER', 'EMPLOYEE')")
    public ResponseEntity<FinishExamResponseDto> finishExam(@RequestBody FinishExamRequestDto dto) {
        FinishExamResponseDto finishDto = examResultService.finishExam(dto);
        return ResponseEntity
                .ok(finishDto);
    }

    @PostMapping("/retake-exam")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MANAGER', 'EMPLOYEE')")
    public ResponseEntity<RetakeExamDto> retakeExam(@RequestBody ExamResultDto dto) {
        RetakeExamDto retakeDto = examResultService.retakeExam(dto.getUserId(), dto.getCourseId());
        return ResponseEntity
                .ok(retakeDto);
    }

    @PostMapping("/accomplishments")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MANAGER', 'EMPLOYEE')")
    public ResponseEntity<List<AccomplishmentsDto>> getAccomplishments(@RequestBody ExamResultDto dto) {
        List<AccomplishmentsDto> accomplishmentsDtos = examResultService.findPassedExamResultByUserId(dto.getUserId());
        return ResponseEntity
                .ok(accomplishmentsDtos);
    }
}
