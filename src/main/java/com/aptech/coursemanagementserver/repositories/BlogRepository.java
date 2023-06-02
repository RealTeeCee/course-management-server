package com.aptech.coursemanagementserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aptech.coursemanagementserver.models.Blog;

public interface BlogRepository extends JpaRepository<Blog, Long> {

}
