package com.aptech.coursemanagementserver.services;

import java.util.List;

import com.aptech.coursemanagementserver.dtos.EnrollmentDto;
import com.aptech.coursemanagementserver.dtos.RatingStarsInterface;
import com.aptech.coursemanagementserver.dtos.baseDto.BaseDto;

public interface EnrollmentService {
    BaseDto enroll(EnrollmentDto enrollmentDto);

    Long getEnrollId(EnrollmentDto enrollmentDto);

    BaseDto updateRating(EnrollmentDto enrollmentDto);

    void updateIsNotify(boolean isNotify, long userId);

    List<RatingStarsInterface> getRatingPercentEachStarsByCourseId(long courseId);
}
