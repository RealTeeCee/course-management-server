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
            DECLARE @session int;
            SET @session = isnull(
                (SELECT max(exam_session) + 1 FROM exam_result WHERE user_id = :userId AND course_id = :courseId)
                ,0)
            INSERT exam_result( [course_id], [anwser_description], [is_correct], [question_description],
            [question_point], [answer_id], [part_id], [question_id], [user_id], exam_session)
            SELECT :courseId, a.description, a.is_correct, q.description, q.point, a.id, q.part_id, q.id, :userId,
            CASE  WHEN @session = 0 THEN 1 ELSE @session END
            FROM question q INNER JOIN answer a
            ON q.id = a.question_id
            WHERE part_id = :partId
            SELECT @session as examSession
                                    """, nativeQuery = true)
    int createExamResultByPartIdAndUserIdAndCourseId(long partId, long userId, long courseId);

    List<ExamResult> findExamResultByPartIdAndUserIdAndExamSession(long partId, long userId, int examSession);

    @Transactional
    @Modifying
    @Query(value = """
            UPDATE exam_result
            SET total_point =
            (SELECT SUM(question_point) total_point FROM exam_result
            WHERE user_id = :userId
            AND part_id = 1
            AND user_answer_id = answer_id
            AND is_correct = 1)
            WHERE part_id = :partId
                                    """, nativeQuery = true)
    void updateExamResultByPartIdAndUserId(long partId, long userId);
}
