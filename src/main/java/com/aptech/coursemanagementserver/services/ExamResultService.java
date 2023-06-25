package com.aptech.coursemanagementserver.services;

import java.util.List;

import com.aptech.coursemanagementserver.dtos.ExamResultResponseDto;

public interface ExamResultService {
    public void createExamResult(long partId, long userId);

    public List<ExamResultResponseDto> findExamResultDetailByPartIdAndUserId(long partId, long userId);
}
