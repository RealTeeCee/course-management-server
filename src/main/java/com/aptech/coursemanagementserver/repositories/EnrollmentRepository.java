package com.aptech.coursemanagementserver.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.aptech.coursemanagementserver.models.Enrollment;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    @Query(value = """
            SELECT e.* FROM enrollment e
            WHERE e.user_id = :userId
                    """, nativeQuery = true)
    List<Enrollment> findAllCoursesByUserId(long userId);
}
