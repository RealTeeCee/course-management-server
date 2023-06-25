package com.aptech.coursemanagementserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aptech.coursemanagementserver.models.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {

}
