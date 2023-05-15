package com.aptech.coursemanagementserver.services;

import java.util.List;

import com.aptech.coursemanagementserver.dtos.LessonDto;
import com.aptech.coursemanagementserver.models.Lesson;

public interface LessonService {
    public Lesson findLessonByName(String lessonName);

    public List<Lesson> findAll();

    public Lesson save(LessonDto lesson);

    public List<Lesson> saveAll(List<LessonDto> lessons);
}
