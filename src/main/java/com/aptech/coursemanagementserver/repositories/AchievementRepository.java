package com.aptech.coursemanagementserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aptech.coursemanagementserver.models.Achievement;

public interface AchievementRepository extends JpaRepository<Achievement, Long> {
    Achievement findAchievementByName(String name);
}