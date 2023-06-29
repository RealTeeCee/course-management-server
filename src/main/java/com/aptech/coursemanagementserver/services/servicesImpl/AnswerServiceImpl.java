package com.aptech.coursemanagementserver.services.servicesImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import com.aptech.coursemanagementserver.dtos.AnswerDto;
import com.aptech.coursemanagementserver.exceptions.BadRequestException;
import com.aptech.coursemanagementserver.models.Answer;
import com.aptech.coursemanagementserver.models.Question;
import com.aptech.coursemanagementserver.repositories.AnswerRepository;
import com.aptech.coursemanagementserver.repositories.ExamResultRepository;
import com.aptech.coursemanagementserver.repositories.QuestionRepository;
import com.aptech.coursemanagementserver.services.AnswerService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnswerServiceImpl implements AnswerService {
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final ExamResultRepository examResultRepository;

    @Override
    public AnswerDto findById(long id) {
        Answer answer = answerRepository.findById(id).orElseThrow(() -> new NoSuchElementException(
                "This answer with answerId: [" + id + "] is not exist."));

        return toDto(answer);
    }

    @Override
    public List<AnswerDto> findAll() {
        List<Answer> answers = answerRepository.findAll();
        List<AnswerDto> answerDtos = new ArrayList<>();

        for (Answer answer : answers) {
            AnswerDto answerDto = toDto(answer);
            answerDtos.add(answerDto);
        }

        return answerDtos;
    }

    @Override
    public void save(AnswerDto answerDto) {
        Answer answer = new Answer();
        if (answerDto.getId() > 0) {
            answer = answerRepository.findById(answerDto.getId()).orElseThrow(
                    () -> new NoSuchElementException(
                            "This answer with answerId: [" + answerDto.getId() + "] is not exist."));
        }

        Question question = questionRepository.findById(answerDto.getQuestionId()).orElseThrow(
                () -> new NoSuchElementException(
                        "This question with questionId: [" + answerDto.getQuestionId() + "] is not exist."));

        int answerCount = question.getAnswers().size() + 1;
        if (answerCount > 4) {
            throw new BadRequestException("There's only 4 options in one question");
        }

        answer.setDescription(answerDto.getDescription());
        answer.setCorrect(answerDto.isCorrect());
        answer.setQuestion(question);

        answerRepository.save(answer);
    }

    @Override
    public void deleteAnswer(long answerId) {
        Answer answer = answerRepository.findById(answerId).orElseThrow(
                () -> new NoSuchElementException("This answer with answerId: [" + answerId + "] is not exist."));

        if (examResultRepository.findByAnswerId(answerId).size() > 0) {
            throw new BadRequestException(
                    "The answer 've already registered in examination.");
        }

        answerRepository.delete(answer);
    }

    @Override
    public AnswerDto toDto(Answer answer) {
        AnswerDto answerDto = AnswerDto.builder()
                .id(answer.getId())
                .description(answer.getDescription())
                .isCorrect(answer.isCorrect())
                .questionId(answer.getQuestion().getId())
                .build();
        return answerDto;
    }
}
