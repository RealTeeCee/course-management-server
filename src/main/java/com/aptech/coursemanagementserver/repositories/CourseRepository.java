package com.aptech.coursemanagementserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aptech.coursemanagementserver.models.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {
    Course findTagByName(String name);
}
