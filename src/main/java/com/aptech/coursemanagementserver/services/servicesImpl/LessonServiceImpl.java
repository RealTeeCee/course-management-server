package com.aptech.coursemanagementserver.services.servicesImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.aptech.coursemanagementserver.dtos.LessonDto;
import com.aptech.coursemanagementserver.models.Lesson;
import com.aptech.coursemanagementserver.repositories.LessonRepository;
import com.aptech.coursemanagementserver.services.LessonService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {
    private final LessonRepository lessonRepository;

    @Override
    public Lesson findLessonByName(String lessonName) {
        return lessonRepository.findLessonByName(lessonName);
    }

    @Override
    public List<Lesson> findAll() {
        return lessonRepository.findAll();
    }

    @Override
    public Lesson save(LessonDto lessonDto) {
        Lesson lesson = lessonRepository.findLessonByName(lessonDto.getName());
        return lessonRepository.save(lesson);
    }

    @Override
    public List<Lesson> saveAll(List<LessonDto> lessonsDto) {
        List<Lesson> lessons = lessonsDto.stream().map(lessonDto -> findLessonByName(lessonDto.getName()))
                .collect(Collectors.toList());
        return lessonRepository.saveAll(lessons);
    }
}
