package com.aptech.coursemanagementserver.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.aptech.coursemanagementserver.dtos.CourseInterface;
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

        @Query(value = """
                        SELECT COUNT(e.id)
                        OVER(PARTITION BY e.user_id) AS enrollmentCount,
                        c.*, c.net_price AS net_price,
                        cat.name AS [category_name] , e.progress, e.rating, e.comment,
                        e.id AS enrollId
                        FROM course c
                        INNER JOIN category cat
                        ON c.category_id = cat.id
                        INNER JOIN enrollment e
                        ON e.course_id = c.id
                        WHERE e.user_id = :userId
                                                    """, nativeQuery = true)
        List<CourseInterface> findAllCoursesByUserId(long userId);

        // @Query(value = """
        // SELECT COUNT(c.id) AS enrollmentCount , c.* , cat.name [category_name],
        // STUFF((SELECT DISTINCT ', ' + a.name
        // FROM course_achievement ca
        // LEFT JOIN achievement a ON ca.achievement_id = a.id
        // WHERE ca.course_id = c.id
        // FOR XML PATH('')),1,1,'') [achievements] ,

        // STUFF((SELECT DISTINCT ', ' + t.name
        // FROM course_tag ct
        // LEFT JOIN tag t ON ct.tag_id = t.id
        // WHERE ct.course_id = c.id
        // FOR XML PATH('')),1,1,'') [tags],

        // ISNULL(e.progress, 0) [progress],
        // --ISNULL(e.rating,0) [rating],
        // ISNULL(e.comment,'No comment') [comment]
        // FROM course c
        // LEFT JOIN enrollment e ON c.id = e.course_id
        // INNER JOIN category cat ON c.category_id = cat.id
        // LEFT JOIN users u ON e.user_id = u.id AND u.role = 'USER'
        // --WHERE u.role = 'USER'
        // GROUP BY
        // e.comment, e.progress,
        // --e.rating ,
        // cat.name, c.[id], c.[created_at], [description], [duration], c.rating ,
        // c.published_at,
        // [image], [level], c.[name], [net_price], [price], [slug], [status],
        // c.[updated_at], [category_id]
        // ORDER BY
        // --c.ordered DESC
        // c.created_at DESC
        // """, nativeQuery = true)
        // List<CourseInterface> findAllCourses();
        @Query(value = """
                        SELECT
                        --COUNT(c.id) AS enrollmentCount ,
                        c.* , cat.name [category_name],

                        STUFF((SELECT DISTINCT ', ' + a.name
                        FROM  course_achievement ca
                        LEFT JOIN achievement a ON ca.achievement_id = a.id
                        WHERE ca.course_id = c.id
                        FOR XML PATH('')),1,1,'') [achievements] ,

                        STUFF((SELECT DISTINCT ', ' + t.name
                        FROM  course_tag ct
                        LEFT JOIN tag t ON ct.tag_id = t.id
                        WHERE ct.course_id = c.id
                        FOR XML PATH('')),1,1,'') [tags],

                        ISNULL(e.progress, 0) [progress],
                        --ISNULL(e.rating,0) [rating],
                        ISNULL(e.comment,'No comment') [comment],
                        COUNT(CASE WHEN(u.role = 'USER') THEN u.role END) [enrollmentCount]
                        FROM course c
                        LEFT JOIN enrollment e ON c.id = e.course_id
                        INNER JOIN category cat ON c.category_id = cat.id
                        LEFT JOIN users u ON e.user_id = u.id AND u.role = 'USER'
                        --WHERE u.role = 'USER'
                        GROUP BY
                        e.comment, e.progress,
                        --e.rating ,
                        cat.name, c.[id], c.[created_at], [description], [duration], c.rating , c.published_at,
                        [image], [level], c.[name], [net_price], [price], [slug], [status], c.[updated_at], [category_id]
                        ORDER BY
                        --c.ordered DESC
                        c.created_at DESC
                                                          """, nativeQuery = true)
        List<CourseInterface> findAllCourses();

        @Query(value = """
                        SELECT TOP 1 COUNT(c.id) AS enrollmentCount, c.id
                        FROM course c
                        LEFT JOIN enrollment e ON c.id = e.course_id
                        INNER JOIN category cat ON c.category_id = cat.id
                        WHERE c.id = :courseId
                        GROUP BY  c.id
                                                """, nativeQuery = true)
        int findEnrollemntCountByCourseId(long courseId);

        @Modifying
        @Transactional
        @Query(value = """
                        UPDATE c
                        SET c.duration = isnull( (
                          SELECT SUM(l.duration)
                          FROM lesson l
                          INNER JOIN section s ON l.section_id = s.id
                          WHERE s.course_id = c.id
                        ),0)
                        FROM course c
                        WHERE c.id = :courseId
                                """, nativeQuery = true)
        void updateCourseDuration(long courseId);

        Course findByName(String courseName);

        Optional<Course> findBySlug(String slug);
}
