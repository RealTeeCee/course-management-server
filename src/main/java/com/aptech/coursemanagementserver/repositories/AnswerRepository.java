package com.aptech.coursemanagementserver.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aptech.coursemanagementserver.models.Answer;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findByQuestionId(long questionId);
}
