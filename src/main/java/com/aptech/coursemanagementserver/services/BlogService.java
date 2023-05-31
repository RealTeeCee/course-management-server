package com.aptech.coursemanagementserver.services;

import java.util.List;

import com.aptech.coursemanagementserver.dtos.BlogDto;
import com.aptech.coursemanagementserver.models.Blog;

public interface BlogService {
    public Blog findBlogByName(String blogName);

    public List<Blog> findAll();

    public boolean save(BlogDto blog);

    public boolean saveAll(List<BlogDto> blogs);
}
