package com.aptech.coursemanagementserver.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.aptech.coursemanagementserver.models.Lesson;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
        Lesson findLessonByName(String name);

        @Query(value = """
                        SELECT TOP 1 l.id FROM lesson l
                        ORDER BY l.id DESC
                        """, nativeQuery = true)
        long findLastLessonId();

        @Query(value = """
                        SELECT l FROM Lesson l INNER JOIN Section s\s
                        ON l.section.id = s.id\s
                        WHERE s.id = :sectionId
                        """)
        List<Lesson> findAllBySectionId(long sectionId);

        @Query(value = """
                        SELECT l.* FROM lesson l
                        JOIN section s
                        ON l.section_id = s.id
                        JOIN course c
                        ON s.course_id = c.id
                        WHERE c.id = :courseId
                            """, nativeQuery = true)
        List<Lesson> findAllByCourseId(long courseId);
}
