package com.aptech.coursemanagementserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aptech.coursemanagementserver.models.Answer;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

}
