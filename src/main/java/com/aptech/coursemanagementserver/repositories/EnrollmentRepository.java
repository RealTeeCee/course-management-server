package com.aptech.coursemanagementserver.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.aptech.coursemanagementserver.dtos.RatingStarsInterface;
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
        JOIN users u ON e.user_id = u.id
        WHERE e.course_id = c.id
        AND u.role = 'USER'
      ),0)
      FROM course c
              """, nativeQuery = true)
  void ratingProcess();

  @Query(value = """
        with rating as
      (
      SELECT COUNT(ISNULL(s.Rating,0)) CNT, s.Rating FROM
      (
      SELECT
      CASE WHEN(e.rating > 0 AND e.rating <= 1)  THEN 1
      WHEN (e.rating > 1 AND e.rating <= 2) THEN 2
      WHEN (e.rating > 2 AND e.rating <= 3)  THEN 3
      WHEN (e.rating > 3 AND e.rating <= 4)  THEN 4
      ELSE 5
      END  as Rating
      FROM enrollment e

      WHERE e.course_id = :courseId) s
      group by s.Rating
      )

      SELECT a.Rating star,
      ISNULL(ROUND( r.cnt * 100/cast((select sum(cnt) from rating) as float),2) ,0) as ratio
      from rating r right join (
      select 1 as Rating, 0 as CNT
      union all
      select 2 as Rating, 0 as CNT
      union all
      select 3 as Rating, 0 as CNT
      union all
      select 4 as Rating, 0 as CNT
      union all
      select 5 as Rating, 0 as CNT
      ) as A on r.Rating = a.Rating
        """, nativeQuery = true)
  List<RatingStarsInterface> getRatingPercentEachStarsByCourseId(long courseId);

  // @Query(value = """
  // SELECT c.author_id FROM course c
  // INNER JOIN enrollment e
  // ON c.id = e.course_id
  // WHERE user_id =:userId
  // """, nativeQuery = true)
  // Enrollment getEnrollByCourseIdAndUserId(long courseId, long userId);
}
