package com.aptech.coursemanagementserver.repositories;

import java.util.List;
import java.util.Optional;

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

    @Query(value = """
            SELECT c.id FROM course c
            JOIN enrollment e
            ON e.course_id = c.id
            GROUP BY c.id
            ORDER BY COUNT(e.course_id) DESC
                    """, nativeQuery = true)
    List<Long> findBestSellerCourseIds();

    Course findByName(String courseName);

    Optional<Course> findBySlug(String slug);
}
