package com.aptech.coursemanagementserver.services.servicesImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.aptech.coursemanagementserver.dtos.AnswerDetailDto;
import com.aptech.coursemanagementserver.dtos.ExamResultResponseDto;
import com.aptech.coursemanagementserver.dtos.FinishExamRequestDto;
import com.aptech.coursemanagementserver.dtos.FinishExamResponseDto;
import com.aptech.coursemanagementserver.dtos.QuestionDto;
import com.aptech.coursemanagementserver.enums.GradeType;
import com.aptech.coursemanagementserver.models.Answer;
import com.aptech.coursemanagementserver.models.ExamResult;
import com.aptech.coursemanagementserver.models.Part;
import com.aptech.coursemanagementserver.models.Question;
import com.aptech.coursemanagementserver.repositories.ExamResultRepository;
import com.aptech.coursemanagementserver.repositories.PartRepository;
import com.aptech.coursemanagementserver.services.AnswerService;
import com.aptech.coursemanagementserver.services.ExamResultService;
import com.aptech.coursemanagementserver.services.QuestionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExamResultServiceImpl implements ExamResultService {
    private final ExamResultRepository examResultRepository;
    private final PartRepository partRepository;
    private final QuestionService questionService;
    private final AnswerService answerService;

    @Override
    public int createExamResult(long userId, long courseId) {
        List<Part> parts = partRepository.findActivePartByCourseId(courseId);
        int partId = 0;
        if (parts.size() > 0) {
            partId = (int) (Math.random() * parts.size()) + 1;
        }

        return examResultRepository.createExamResultByPartIdAndUserIdAndCourseId(partId, userId, courseId);
    }

    @Override
    public List<ExamResultResponseDto> findExamResultByCourseIdAndUserIdAndExamSession(long courseId, long userId,
            int examSession) {
        List<ExamResult> examResults = examResultRepository.findExamResultByCourseIdAndUserIdAndExamSession(courseId,
                userId,
                examSession);

        List<ExamResultResponseDto> examResultDtos = new ArrayList<>();
        Set<Question> questions = examResults.stream().map(e -> e.getQuestion()).collect(Collectors.toSet());

        for (Question question : questions) {

            QuestionDto questionDto = questionService.toDto(question);
            ExamResultResponseDto dto = ExamResultResponseDto.builder()
                    .question(questionDto)
                    .examSession(examSession)
                    .userId(userId)
                    .limitTime(question.getPart().getLimitTime())
                    .courseId(courseId).build();
            Set<Answer> answers = examResults.stream().filter(q -> q.getQuestion().equals(question))
                    .map(e -> e.getAnswer()).collect(Collectors.toSet());
            answers.forEach(a -> {
                dto.getAnswers().add(answerService.toDto(a));
            });

            examResultDtos.add(dto);
        }

        return examResultDtos;
    }

    @Override
    public FinishExamResponseDto finishExam(FinishExamRequestDto dto) {
        List<ExamResult> examResults = examResultRepository.findExamResultByCourseIdAndUserIdAndExamSession(
                dto.getCourseId(),
                dto.getUserId(),
                dto.getExamSession());

        // Tính tổng point answer correct
        var trueAnwserPoint = dto.getAnswers().stream()
                .filter(a -> a.getUserAnswerId() == a.getAnswerId() && a.isCorrect() == true)
                .map(a -> a.getPoint()).toList();

        double totalPoint = trueAnwserPoint.size() == 0 ? 0 : trueAnwserPoint.stream().reduce((a, b) -> a + b).get();

        // double totalPoint = 97.5;
        double maxPoint = examResults.get(0).getPart().getMaxPoint();
        double percentPoint = totalPoint * 100 / maxPoint;
        // 0-40: FAIL 40-65: AVERAGE 65-80: GOOD >=80: EXCELLENT
        GradeType grade = GradeType.FAIL;
        if (percentPoint >= 0 && percentPoint < 40) {
            grade = GradeType.FAIL;
        } else if (percentPoint >= 40 && percentPoint < 65) {
            grade = GradeType.AVERAGE;
        } else if (percentPoint >= 65 && percentPoint < 80) {
            grade = GradeType.GOOD;
        } else {
            grade = GradeType.EXCELLENT;
        }

        for (AnswerDetailDto answer : dto.getAnswers()) {
            final GradeType gradeType = grade;
            examResults.forEach(e -> {
                if (e.getQuestion().getId() == answer.getId()) {
                    e.setUserAnswerId(answer.getUserAnswerId());
                }
                e.setTotalExamTime(dto.getTotalExamTime())
                        .setTotalPoint(totalPoint)
                        .setGrade(gradeType);
            });
        }
        // for (ExamResult examResult : examResults) {
        // try {
        // examResult.setTotalExamTime(dto.getTotalExamTime())
        // .setTotalPoint(totalPoint)
        // .setGrade(grade);
        // } catch (Exception e) {
        // log.error("", e);
        // }

        // }
        examResultRepository.saveAll(examResults);

        FinishExamResponseDto finishDto = FinishExamResponseDto.builder()
                .totalExamTime(dto.getTotalExamTime())
                .totalPoint(totalPoint)
                .grade(grade)
                .build();
        return finishDto;
    }
}
