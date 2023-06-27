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
        List<Part> parts = partRepository.findAllByCourseId(courseId);
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

}
