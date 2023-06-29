package com.aptech.coursemanagementserver.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aptech.coursemanagementserver.models.Subcribes;

public interface SubcribesRepository extends JpaRepository<Subcribes, Long> {
    Subcribes findByAuthorIdAndUserId(long authorId, long userId);

    List<Subcribes> findByAuthorId(long authorId);
}