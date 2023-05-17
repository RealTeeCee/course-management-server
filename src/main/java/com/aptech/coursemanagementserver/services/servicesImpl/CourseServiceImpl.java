package com.aptech.coursemanagementserver.services.servicesImpl;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.aptech.coursemanagementserver.dtos.CourseDto;
import com.aptech.coursemanagementserver.dtos.baseDto.BaseDto;
import com.aptech.coursemanagementserver.enums.AntType;
import com.aptech.coursemanagementserver.mappers.CourseMapper;
import com.aptech.coursemanagementserver.models.Achievement;
import com.aptech.coursemanagementserver.models.Course;
import com.aptech.coursemanagementserver.models.Section;
import com.aptech.coursemanagementserver.models.Tag;
import com.aptech.coursemanagementserver.repositories.AchievementRepository;
import com.aptech.coursemanagementserver.repositories.CategoryRepository;
import com.aptech.coursemanagementserver.repositories.CourseRepository;
import com.aptech.coursemanagementserver.repositories.SectionRepository;
import com.aptech.coursemanagementserver.repositories.TagRepository;
import com.aptech.coursemanagementserver.services.CourseService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;
    private final TagRepository tagRepository;
    private final SectionRepository sectionRepository;
    private final AchievementRepository achievementRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public List<Course> findAllByTagName(String tagName) {
        return courseRepository.findAllByTagName(tagName);
    }

    @Override
    public Course findById(long courseId) {
        return courseRepository.findById(courseId).get();
    }

    @Override
    public Course findByName(String courseName) {
        return courseRepository.findByName(courseName);
    }

    @Override
    public List<Course> findAll() {
        List<Course> courses = courseRepository.findAll();
        return courses;
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

        List<String> sectionsString = courseDto.getSections();
        Set<Section> sections = new HashSet<>();

        if (sectionsString != null) {
            int i = 0;
            while (i < sectionsString.size()) {
                Section section = new Section();
                String sectionString = sectionsString.get(i);
                if (sectionString != null) {
                    section.setName(sectionString);
                    section.setCourse(findByName(courseDto.getName()));
                    sections.add(section);
                }
                i++;
            }
        }

        sectionRepository.saveAll(sections);
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

    @Override
    public BaseDto delete(long courseId) {
        try {
            Course course = courseRepository.findById(courseId).get();
            courseRepository.delete(course);
            return BaseDto.builder().type(AntType.success).message("Delete course successfully.")
                    .build();
        } catch (NoSuchElementException e) {
            return BaseDto.builder().type(AntType.error)
                    .message("This course with courseId: [" + courseId + "] is not exist.")
                    .build();
        } catch (Exception e) {
            return BaseDto.builder().type(AntType.error)
                    .message("Failed! Please check your infomation and try again.")
                    .build();
        }
    }
}
