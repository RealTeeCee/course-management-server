package com.aptech.coursemanagementserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aptech.coursemanagementserver.models.ExamResult;

public interface ExamResultRepository extends JpaRepository<ExamResult, Long> {

}
