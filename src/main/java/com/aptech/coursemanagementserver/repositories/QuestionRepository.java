package com.aptech.coursemanagementserver.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aptech.coursemanagementserver.models.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByPartId(long partId);
}
