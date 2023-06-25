package com.aptech.coursemanagementserver.services.servicesImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import com.aptech.coursemanagementserver.dtos.QuestionDto;
import com.aptech.coursemanagementserver.exceptions.BadRequestException;
import com.aptech.coursemanagementserver.models.Part;
import com.aptech.coursemanagementserver.models.Question;
import com.aptech.coursemanagementserver.repositories.PartRepository;
import com.aptech.coursemanagementserver.repositories.QuestionRepository;
import com.aptech.coursemanagementserver.services.QuestionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {
    private final QuestionRepository questionRepository;
    private final PartRepository partRepository;

    @Override
    public QuestionDto findById(long id) {
        Question question = questionRepository.findById(id).orElseThrow(() -> new NoSuchElementException(
                "This question with questionId: [" + id + "] is not exist."));

        return toDto(question);
    }

    @Override
    public List<QuestionDto> findAll() {
        List<Question> questions = questionRepository.findAll();
        List<QuestionDto> questionDtos = new ArrayList<>();

        for (Question question : questions) {
            QuestionDto questionDto = toDto(question);
            questionDtos.add(questionDto);
        }

        return questionDtos;
    }

    @Override
    public void save(QuestionDto questionDto) {
        Question question = new Question();
        if (questionDto.getId() > 0) {
            question = questionRepository.findById(questionDto.getId()).orElseThrow(
                    () -> new NoSuchElementException(
                            "This question with questionId: [" + questionDto.getId() + "] is not exist."));
        }

        Part part = partRepository.findById(questionDto.getPartId()).orElseThrow(
                () -> new NoSuchElementException(
                        "This part with partId: [" + questionDto.getPartId() + "] is not exist."));

        question.setDescription(questionDto.getDescription());
        question.setPart(part);
        if (part.getQuestions().stream().mapToDouble(q -> q.getPoint()).sum() + questionDto.getPoint() > part
                .getMaxPoint()) {
            throw new BadRequestException(
                    "The total points for this part's questions exceeds the maximum allowed points.");
        }
        question.setPoint(question.getPoint());

        questionRepository.save(question);
    }

    @Override
    public void deleteQuestion(long questionId) {
        Question question = questionRepository.findById(questionId).orElseThrow(
                () -> new NoSuchElementException("This question with questionId: [" + questionId + "] is not exist."));

        if (question.getAnswers().size() > 0) {
            throw new BadRequestException(
                    "The question 've already have answer");
        }

        questionRepository.delete(question);
    }

    private QuestionDto toDto(Question question) {
        QuestionDto questionDto = QuestionDto.builder()
                .id(question.getId())
                .description(question.getDescription())
                .point(question.getPoint())
                .partId(question.getPart().getId())
                .build();
        return questionDto;
    }
}
