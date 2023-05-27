package com.aptech.coursemanagementserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aptech.coursemanagementserver.models.LessonTracking;
import com.aptech.coursemanagementserver.models.LessonTrackingId;

public interface LessonTrackingRepository extends JpaRepository<LessonTracking, LessonTrackingId> {

}
