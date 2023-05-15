package com.aptech.coursemanagementserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aptech.coursemanagementserver.models.Lesson;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
    Lesson findLessonByName(String name);
}
