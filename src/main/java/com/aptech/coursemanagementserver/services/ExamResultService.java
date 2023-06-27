package com.aptech.coursemanagementserver.services;

import java.util.List;

import com.aptech.coursemanagementserver.dtos.ExamResultResponseDto;

public interface ExamResultService {
    public int createExamResult(long userId, long courseId);

    public List<ExamResultResponseDto> findExamResultByCourseIdAndUserIdAndExamSession(long courseId, long userId,
            int examSession);
}
