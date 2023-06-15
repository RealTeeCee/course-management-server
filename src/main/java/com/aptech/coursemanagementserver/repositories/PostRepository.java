package com.aptech.coursemanagementserver.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aptech.coursemanagementserver.models.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findById(long id);

}
