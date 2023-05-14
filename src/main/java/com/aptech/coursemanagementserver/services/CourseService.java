package com.aptech.coursemanagementserver.services;

import java.util.List;
import java.util.Set;

import com.aptech.coursemanagementserver.dtos.CourseDto;
import com.aptech.coursemanagementserver.models.Achievement;
import com.aptech.coursemanagementserver.models.Course;
import com.aptech.coursemanagementserver.models.Tag;

public interface CourseService {
    public CourseDto findTagByName(String courseName);

    public List<CourseDto> findAll();

    public Course save(CourseDto course);

    public List<Course> saveAll(List<CourseDto> courses);

    public Set<Tag> splitTag(String tag);

    public Set<Achievement> splitAchievement(String achievement);
}