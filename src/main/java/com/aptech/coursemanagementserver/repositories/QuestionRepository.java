package com.aptech.coursemanagementserver.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.aptech.coursemanagementserver.models.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    @Query(value = """
            SELECT q.* FROM question q WHERE q.part_id = :partId
            ORDER BY created_at DESC
            """, nativeQuery = true)
    List<Question> findByPartId(long partId);
}
