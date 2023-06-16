package com.aptech.coursemanagementserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aptech.coursemanagementserver.models.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

}
