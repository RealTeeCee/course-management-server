package com.aptech.coursemanagementserver.services;

import java.util.List;

import com.aptech.coursemanagementserver.dtos.LessonDto;
import com.aptech.coursemanagementserver.dtos.baseDto.BaseDto;
import com.aptech.coursemanagementserver.models.Lesson;

public interface LessonService {
    public Lesson findLessonByName(String lessonName);

    public List<Lesson> findAll();

    public List<LessonDto> findAllBySectionId(long sectionId);

    public BaseDto saveLessonsToSection(LessonDto lessonDto, long sectionId);

    public BaseDto updateLesson(LessonDto lessonDto, long lessonId);

    public BaseDto delete(long lessonId);

}
