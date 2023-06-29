package com.aptech.coursemanagementserver.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.aptech.coursemanagementserver.models.Part;

public interface PartRepository extends JpaRepository<Part, Long> {
    @Query(value = """
            SELECT p.* FROM part p WHERE p.course_id = :courseId
            """, nativeQuery = true)
    List<Part> findAllByCourseId(long courseId);
}