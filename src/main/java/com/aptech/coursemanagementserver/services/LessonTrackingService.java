package com.aptech.coursemanagementserver.services;

import com.aptech.coursemanagementserver.dtos.LearningDto;
import com.aptech.coursemanagementserver.dtos.LessonTrackingDto;

public interface LessonTrackingService {
    LessonTrackingDto loadTrack(LessonTrackingDto lessonTrackingDto);

    boolean saveTrack(LessonTrackingDto lessonTrackingDto);

    boolean complete(LessonTrackingDto lessonTrackingDto);

    boolean updateProgress(long enrollmentId, long courseId);

    LearningDto getLearnDetails(long courseId);
}
