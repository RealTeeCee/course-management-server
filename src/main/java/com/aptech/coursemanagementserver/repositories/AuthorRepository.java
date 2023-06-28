package com.aptech.coursemanagementserver.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.aptech.coursemanagementserver.models.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    @Query(value = """
            SELECT * FROM author ORDER BY created_at DESC
            """, nativeQuery = true)
    List<Author> findAll();
}