package com.aptech.coursemanagementserver.services;

import com.aptech.coursemanagementserver.dtos.LearningDto;
import com.aptech.coursemanagementserver.dtos.LessonTrackingDto;

public interface LessonTrackingService {
    LessonTrackingDto loadTrack(LessonTrackingDto lessonTrackingDto);

    boolean saveTrack(LessonTrackingDto lessonTrackingDto);

    double complete(LessonTrackingDto lessonTrackingDto);

    double updateProgress(long enrollmentId, long courseId);

    LearningDto getLearnDetails(long courseId);
}
