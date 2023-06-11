package com.aptech.coursemanagementserver.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aptech.coursemanagementserver.enums.BlogStatus;
import com.aptech.coursemanagementserver.models.Blog;

public interface BlogRepository extends JpaRepository<Blog, Long> {
    Blog findBlogByName(String name);

    List<Blog> findByUserId(long userId);

    List<Blog> findByStatus(BlogStatus status);

}
