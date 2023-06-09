package com.aptech.coursemanagementserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.aptech.coursemanagementserver.models.Enrollment;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

  @Query(value = """
      SELECT * FROM enrollment
      WHERE course_id =:courseId
      AND user_id =:userId
          """, nativeQuery = true)
  Enrollment getEnrollByCourseIdAndUserId(long courseId, long userId);

  @Modifying
  @Transactional
  @Query(value = """
      UPDATE c
      SET c.rating = isnull( (
        SELECT AVG(e.rating)
        FROM enrollment e
        WHERE e.course_id = c.id
      ),0)
      FROM course c
              """, nativeQuery = true)
  void ratingProcess();
}
