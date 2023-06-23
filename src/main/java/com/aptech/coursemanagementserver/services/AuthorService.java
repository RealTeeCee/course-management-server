package com.aptech.coursemanagementserver.services;

import java.util.List;

import com.aptech.coursemanagementserver.dtos.AuthorDto;

public interface AuthorService {

    public AuthorDto findById(long id);

    public List<AuthorDto> findAll();

    public void save(AuthorDto authorDto);

    public void saveAll(List<AuthorDto> authorDtos);

    public void deleteAuthor(long authorId);
}
