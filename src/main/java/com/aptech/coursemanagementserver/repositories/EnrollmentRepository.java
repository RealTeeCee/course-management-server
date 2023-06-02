package com.aptech.coursemanagementserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.aptech.coursemanagementserver.models.Enrollment;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    @Query(value = """
            select *
            from enrollment where course_id =:courseId and user_id =:userId
                """, nativeQuery = true)
    Enrollment getEnrollByCourseIdAndUserId(long courseId, long userId);
}
