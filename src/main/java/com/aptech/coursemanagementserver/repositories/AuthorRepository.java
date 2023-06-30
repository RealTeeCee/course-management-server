package com.aptech.coursemanagementserver.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.aptech.coursemanagementserver.dtos.AuthorInterface;
import com.aptech.coursemanagementserver.models.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {
        @Query(value = """
                        SELECT * FROM author ORDER BY created_at DESC
                        """, nativeQuery = true)
        List<Author> findAll();

        @Query(value = """
                        SELECT TOP 3 b.* FROM (
                        SELECT COUNT (a1.authorId) enrollmentCount, [id], [created_at], [image], [name]
                        FROM author a LEFT JOIN
                        (
                        SELECT  c.author_id AuthorId ,c.id courseId FROM author a
                        LEFT JOIN course c ON a.id = c.author_id
                        INNER JOIN enrollment e ON c.id = e.course_id
                        GROUP BY author_id,  c.id

                        ) AS a1 ON a.id = a1.AuthorId

                        GROUP BY [id], [created_at], [image], a.[name]
                        ) b
                        ORDER BY b.enrollmentCount DESC
                                    """, nativeQuery = true)
        List<AuthorInterface> findTop3();

}