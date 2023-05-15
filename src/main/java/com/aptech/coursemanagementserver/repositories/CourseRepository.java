package com.aptech.coursemanagementserver.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.aptech.coursemanagementserver.models.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {
    @Query("""
                SELECT c FROM Course c
                JOIN c.tags t
                WHERE t.name = :tagName
            """)
    List<Course> findAllByTagName(String tagName);
}
