package com.aptech.coursemanagementserver.services;

import java.util.List;
import java.util.Set;

import com.aptech.coursemanagementserver.dtos.CourseDto;
import com.aptech.coursemanagementserver.dtos.baseDto.BaseDto;
import com.aptech.coursemanagementserver.models.Achievement;
import com.aptech.coursemanagementserver.models.Course;
import com.aptech.coursemanagementserver.models.Tag;

public interface CourseService {
    public List<Course> findAllByTagName(String tagName);

    public Course findCourseById(long CourseId);

    public CourseDto findById(long courseId);

    public Course findByName(String courseName);

    public List<CourseDto> findAll();

    public List<CourseDto> findFreeCourses();

    public List<CourseDto> findRelatedCourses(long categoryId, long tagId);

    public Course save(Course course);

    public Course setProperties(CourseDto courseDto, Course course);

    public Course save(CourseDto course);

    public List<Course> saveAll(List<CourseDto> courses);

    public Set<Tag> splitTag(String tag, Course course);

    public Set<Achievement> splitAchievement(String achievement, Course course);

    public BaseDto delete(long courseId);
}