package com.aptech.coursemanagementserver.services;

import java.util.List;

import com.aptech.coursemanagementserver.dtos.AuthorDto;
import com.aptech.coursemanagementserver.dtos.AuthorInterface;

public interface AuthorService {

    public AuthorDto findById(long id);

    public List<AuthorDto> findAll();

    public List<AuthorDto> findAllPagination(int pageNo, int pageSize);

    public List<AuthorInterface> findTop3();

    public void save(AuthorDto authorDto);

    public void saveAll(List<AuthorDto> authorDtos);

    public void deleteAuthor(long authorId);
}
