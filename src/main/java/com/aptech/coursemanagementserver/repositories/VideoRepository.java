package com.aptech.coursemanagementserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aptech.coursemanagementserver.models.Video;

public interface VideoRepository extends JpaRepository<Video, Long> {
    Video findVideoByName(String name);
}