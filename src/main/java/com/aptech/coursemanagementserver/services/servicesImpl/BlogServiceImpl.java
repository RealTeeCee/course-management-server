package com.aptech.coursemanagementserver.services.servicesImpl;

import java.util.List;

import com.aptech.coursemanagementserver.dtos.BlogDto;
import com.aptech.coursemanagementserver.models.Blog;
import com.aptech.coursemanagementserver.services.BlogService;

public class BlogServiceImpl implements BlogService {

    @Override
    public Blog findBlogByName(String blogName) {
        return null;
    }

    @Override
    public List<Blog> findAll() {
        return null;
    }

    @Override
    public boolean save(BlogDto blog) {
        return true;
    }

    @Override
    public boolean saveAll(List<BlogDto> blogs) {
        return true;
    }

}
