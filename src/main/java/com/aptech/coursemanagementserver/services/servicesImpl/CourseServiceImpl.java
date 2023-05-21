package com.aptech.coursemanagementserver.services.servicesImpl;

import static com.aptech.coursemanagementserver.constants.GlobalStorage.BAD_REQUEST_EXCEPTION;

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
import com.github.slugify.Slugify;

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
    public Course save(Course course) {
        return courseRepository.save(course);
    }

    @Override
    public Course save(CourseDto courseDto) {
        Course course = new Course();

        setProperties(courseDto, course);
        return course;
    }

    public Course setProperties(CourseDto courseDto, Course course) {

        course.setName(courseDto.getName().replaceAll("\\s{2,}", " "))
                .setCategory(categoryRepository.findById(courseDto.getCategory()).get())
                .setTags(splitTag(courseDto.getTagName()))
                .setAchievements(
                        splitAchievement(courseDto.getAchievementName(), course))
                .setImage(courseDto.getImage())
                .setSlug(Slugify.builder().build().slugify(courseDto.getName()))
                .setDuration(courseDto.getDuration())
                .setDescription(courseDto.getDescription())
                .setPrice(courseDto.getPrice())
                .setNet_price(courseDto.getNet_price());

        courseRepository.save(course);

        List<String> sectionsString = courseDto.getSections();
        Set<Section> sections = course.getSections();

        if (sectionsString != null) {
            int i = 0;
            while (i < sectionsString.size()) {
                Section section = new Section();
                String sectionString = sectionsString.get(i);
                if (sectionString != null) {
                    section.setName(sectionString);
                    section.setCourse(course);
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
    public Set<Achievement> splitAchievement(String achievement, Course course) {

        boolean isUpdatedCourse = course.getId() > 0 ? true : false;

        // Check achievement if not exist add new.
        Set<Achievement> newAchievements;
        Set<Achievement> updatedAchievements = new HashSet<>();

        if (isUpdatedCourse) {
            newAchievements = course.getAchievements();
        } else {
            newAchievements = new HashSet<>();
        }

        // Set<Achievement> newAchievements = new HashSet<>();

        String[] achievements = achievement.split(",");
        for (String achievementName : achievements) {
            if (isUpdatedCourse) {
                Achievement achievementOfCourse = achievementRepository
                        .findAchievementByNameAndCourseId(achievementName, course.getId());
                if (achievementOfCourse != null && achievementOfCourse.getName() == achievementName) {
                    updatedAchievements.add(achievementOfCourse);
                    continue;
                }

                Achievement updatedAchievement = new Achievement();
                updatedAchievement.setName(achievementName);
                updatedAchievements.add(updatedAchievement);
                continue;
            }

            if (achievementRepository.findAchievementByName(achievementName) == null) {
                Achievement newAchievement = new Achievement();
                newAchievement.setName(achievementName);
                newAchievements.add(newAchievement);
            }
        }

        if (isUpdatedCourse) {
            achievementRepository.deleteAll(newAchievements);
            newAchievements.removeAll(newAchievements);

            newAchievements.addAll(updatedAchievements);
        }

        achievementRepository.saveAll(newAchievements);
        // Asign achievement of course by set achievement generated from string
        // achievement.
        /*
         * Set<Achievement> achievementsOfCourse = course.getAchievements();
         * achievementsOfCourse.removeAll(achievementsOfCourse);
         * for (String achievementName : achievements) {
         * 
         * Achievement newAchievement = new Achievement();
         * newAchievement.setName(achievementName);
         * newAchievements.add(newAchievement);
         * 
         * // If not contain -> add
         * if (!achievementsOfCourse.contains(newAchievement)) {
         * achievementsOfCourse.add(newAchievement);
         * }
         * }
         * achievementRepository.saveAll(newAchievements);
         * 
         * return isUpdatedCourse ? achievementsOfCourse : newAchievements;
         */
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
                    .message(BAD_REQUEST_EXCEPTION)
                    .build();
        }
    }
}
