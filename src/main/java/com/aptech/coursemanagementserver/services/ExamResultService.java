package com.aptech.coursemanagementserver.services;

import java.util.List;

import com.aptech.coursemanagementserver.dtos.ExamResultResponseDto;

public interface ExamResultService {
    public int createExamResult(long partId, long userId, long courseId);

    public List<ExamResultResponseDto> findExamResultByPartIdAndUserIdAndExamSession(long partId, long userId,
            int examSession);
}
