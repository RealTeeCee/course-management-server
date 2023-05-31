package com.aptech.coursemanagementserver.services;

import java.util.List;

import com.aptech.coursemanagementserver.dtos.EnrollmentDto;
import com.aptech.coursemanagementserver.dtos.baseDto.BaseDto;

public interface EnrollmentService {
    BaseDto enroll(EnrollmentDto enrollmentDto);

    List<EnrollmentDto> findCoursesByUserId(long userId);

}
