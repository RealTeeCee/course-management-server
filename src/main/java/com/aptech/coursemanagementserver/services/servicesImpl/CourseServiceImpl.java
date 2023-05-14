package com.aptech.coursemanagementserver.services.servicesImpl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.aptech.coursemanagementserver.dtos.CourseDto;
import com.aptech.coursemanagementserver.mappers.CourseMapper;
import com.aptech.coursemanagementserver.models.Achievement;
import com.aptech.coursemanagementserver.models.Course;
import com.aptech.coursemanagementserver.models.Tag;
import com.aptech.coursemanagementserver.repositories.AchievementRepository;
import com.aptech.coursemanagementserver.repositories.CategoryRepository;
import com.aptech.coursemanagementserver.repositories.CourseRepository;
import com.aptech.coursemanagementserver.repositories.TagRepository;
import com.aptech.coursemanagementserver.services.CourseService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;
    private final TagRepository tagRepository;
    private final AchievementRepository achievementRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public CourseDto findTagByName(String name) {
        Course course = courseRepository.findTagByName(name);
        return courseMapper.toDto(course);
    }

    @Override
    public List<CourseDto> findAll() {
        List<Course> courses = courseRepository.findAll();
        return courseMapper.toDtoList(courses);
    }

    @Override
    public Course save(CourseDto courseDto) {
        Course course = new Course();

        course.setName(courseDto.getName().replaceAll("\\s{2,}", " "))
                .setCategory(categoryRepository.findById(courseDto.getCategory()).get())
                .setTags(splitTag(courseDto.getTagName()))
                .setAchievements(splitAchievement(courseDto.getAchievementName()))
                .setImage(courseDto.getImage())
                .setSlug(courseDto.getName().toLowerCase().replaceAll("\\s{2,}", " ").replace(" ", "-"))
                .setDuration(courseDto.getDuration())
                .setDescription(courseDto.getDescription())
                .setPrice(courseDto.getPrice())
                .setNet_price(courseDto.getNet_price());

        courseRepository.save(course);
        return course;
    }

    @Override
    public List<Course> saveAll(List<CourseDto> coursesDto) {
        List<Course> courses = courseMapper.toEntityList(coursesDto);
        courseRepository.saveAll(courses);
        return courses;
    }

    @Override
    public Set<Tag> splitTag(String tag) {

        Set<Tag> newTags = new HashSet<>();
        String[] tags = tag.split(",");
        for (String tagName : tags) {
            if (tagRepository.findTagByName(tagName) == null) {
                Tag newTag = new Tag();
                newTag.setName(tagName);
                newTags.add(newTag);
            }
        }
        tagRepository.saveAll(newTags);
        return newTags;
    }

    @Override
    public Set<Achievement> splitAchievement(String achievement) {
        Set<Achievement> newAchievements = new HashSet<>();
        String[] achievements = achievement.split(",");
        for (String achievementName : achievements) {
            if (achievementRepository.findAchievementByName(achievementName) == null) {
                Achievement newAchievement = new Achievement();
                newAchievement.setName(achievementName);
                newAchievements.add(newAchievement);
            }
        }
        achievementRepository.saveAll(newAchievements);
        return newAchievements;
    }

}
