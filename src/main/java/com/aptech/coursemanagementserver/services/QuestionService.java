package com.aptech.coursemanagementserver.services;

import java.util.List;

import com.aptech.coursemanagementserver.dtos.QuestionDto;

public interface QuestionService {
    public QuestionDto findById(long id);

    public List<QuestionDto> findAll();

    public void save(QuestionDto questionDto);

    public void deleteQuestion(long id);
}
