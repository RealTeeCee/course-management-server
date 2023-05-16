package com.aptech.coursemanagementserver.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.aptech.coursemanagementserver.models.Lesson;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
    Lesson findLessonByName(String name);

    @Query(value = """
            SELECT l FROM Lesson l INNER JOIN Section s\s
            ON l.section.id = s.id\s
            WHERE s.id = :sectionId
            """)
    List<Lesson> findAllBySectionId(long sectionId);
}
