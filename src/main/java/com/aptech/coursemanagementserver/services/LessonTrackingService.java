package com.aptech.coursemanagementserver.services;

import com.aptech.coursemanagementserver.dtos.LessonTrackingDto;

public interface LessonTrackingService {
    boolean trackLesson(LessonTrackingDto lessonTrackingDto);

}
