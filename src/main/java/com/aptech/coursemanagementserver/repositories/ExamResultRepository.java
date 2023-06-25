package com.aptech.coursemanagementserver.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.aptech.coursemanagementserver.models.ExamResult;

import jakarta.transaction.Transactional;

public interface ExamResultRepository extends JpaRepository<ExamResult, Long> {
    List<ExamResult> findByPartId(long partId);

    List<ExamResult> findByQuestionId(long questionId);

    List<ExamResult> findByAnswerId(long answerId);

    @Transactional
    @Modifying
    @Query(value = """
            INSERT exam_result([anwser_description], [is_correct], [question_description],
            [question_point], [answer_id], [part_id], [question_id], [user_id])
            SELECT a.description, a.is_correct, q.description, q.point, a.id, q.part_id, q.id, :userId
            FROM question q INNER JOIN answer a
            ON q.id = a.question_id
            WHERE part_id = :partId
                        """, nativeQuery = true)
    void createExamResultByPartIdAndUserId(long partId, long userId);

    List<ExamResult> findExamResultByPartIdAndUserId(long partId, long userId);
}
