package com.aptech.coursemanagementserver.services;

import com.aptech.coursemanagementserver.dtos.EnrollmentDto;
import com.aptech.coursemanagementserver.dtos.baseDto.BaseDto;

public interface EnrollmentService {
    BaseDto enroll(EnrollmentDto enrollmentDto);

    Long getEnrollId(EnrollmentDto enrollmentDto);

    BaseDto updateRating(EnrollmentDto enrollmentDto);
}
