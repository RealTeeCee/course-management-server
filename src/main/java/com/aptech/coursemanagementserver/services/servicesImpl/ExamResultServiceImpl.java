package com.aptech.coursemanagementserver.services.servicesImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.aptech.coursemanagementserver.dtos.ExamResultResponseDto;
import com.aptech.coursemanagementserver.dtos.QuestionDto;
import com.aptech.coursemanagementserver.models.Answer;
import com.aptech.coursemanagementserver.models.ExamResult;
import com.aptech.coursemanagementserver.models.Question;
import com.aptech.coursemanagementserver.repositories.ExamResultRepository;
import com.aptech.coursemanagementserver.services.AnswerService;
import com.aptech.coursemanagementserver.services.ExamResultService;
import com.aptech.coursemanagementserver.services.QuestionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExamResultServiceImpl implements ExamResultService {
    private final ExamResultRepository examResultRepository;
    private final QuestionService questionService;
    private final AnswerService answerService;

    @Override
    public int createExamResult(long partId, long userId, long courseId) {
        return examResultRepository.createExamResultByPartIdAndUserIdAndCourseId(partId, userId, courseId);
    }

    @Override
    public List<ExamResultResponseDto> findExamResultByPartIdAndUserIdAndExamSession(long partId, long userId,
            int examSession) {
        List<ExamResult> examResults = examResultRepository.findExamResultByPartIdAndUserIdAndExamSession(partId,
                userId,
                examSession);

        List<ExamResultResponseDto> examResultDtos = new ArrayList<>();
        Set<Question> questions = examResults.stream().map(e -> e.getQuestion()).collect(Collectors.toSet());

        for (Question question : questions) {

            QuestionDto questionDto = questionService.toDto(question);
            ExamResultResponseDto dto = ExamResultResponseDto.builder().question(questionDto).build();
            Set<Answer> answers = examResults.stream().filter(q -> q.getQuestion().equals(question))
                    .map(e -> e.getAnswer()).collect(Collectors.toSet());
            answers.forEach(a -> {
                dto.getAnswers().add(answerService.toDto(a));
            });

            examResultDtos.add(dto);
        }

        return examResultDtos;
    }

}
