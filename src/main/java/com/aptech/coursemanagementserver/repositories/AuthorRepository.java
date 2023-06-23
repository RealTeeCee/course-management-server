package com.aptech.coursemanagementserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aptech.coursemanagementserver.models.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}