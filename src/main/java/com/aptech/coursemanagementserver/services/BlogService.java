package com.aptech.coursemanagementserver.services;

import java.util.List;

import com.aptech.coursemanagementserver.dtos.BlogDto;
import com.aptech.coursemanagementserver.dtos.baseDto.BaseDto;
import com.aptech.coursemanagementserver.models.Blog;

public interface BlogService {
    public Blog findBlogByName(String blogName);

    public List<BlogDto> findAll();

    public BaseDto create(BlogDto blogDto);

    public BaseDto update(BlogDto blogDto);

    public BaseDto delete(long blogId);

    public BlogDto findById(long blogId);
}
