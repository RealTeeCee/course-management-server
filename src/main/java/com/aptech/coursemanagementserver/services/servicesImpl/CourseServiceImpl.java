package com.aptech.coursemanagementserver.services.servicesImpl;

import static com.aptech.coursemanagementserver.constants.GlobalStorage.BAD_REQUEST_EXCEPTION;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.aptech.coursemanagementserver.dtos.CourseDto;
import com.aptech.coursemanagementserver.dtos.baseDto.BaseDto;
import com.aptech.coursemanagementserver.enums.AntType;
import com.aptech.coursemanagementserver.exceptions.BadRequestException;
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
    public Course findCourseById(long courseId) {
        return courseRepository.findById(courseId).orElseThrow(
                () -> new NoSuchElementException("This course with courseId: [" + courseId + "] is not exist."));
    }

    @Override
    public CourseDto findById(long courseId) {
        Course course = courseRepository.findById(courseId).orElseThrow(
                () -> new NoSuchElementException("This course with courseId: [" + courseId + "] is not exist."));

        CourseDto courseDto = toCourseDto(course);

        return courseDto;
    }

    @Override
    public CourseDto findBySlug(String slug) {
        Course course = courseRepository.findBySlug(slug).orElseThrow(
                () -> new NoSuchElementException("This course with slug: [" + slug + "] is not exist."));

        CourseDto courseDto = toCourseDto(course);

        return courseDto;
    }

    @Override
    public Course findByName(String courseName) {
        return courseRepository.findByName(courseName);
    }

    @Override
    public List<CourseDto> findBestSellerCourses() {
        // Free Course: Query theo enrolled, count thằng nào enrolled nhiều nhất là lên
        // top đầu
        List<Long> courseIds = courseRepository.findBestSellerCourseIds();

        List<CourseDto> courseDtos = new ArrayList<>();
        for (Long courseId : courseIds) {
            Course course = courseRepository.findById(courseId).get();
            if (course.getStatus() == 1) {
                CourseDto courseDto = toCourseDto(course);
                courseDtos.add(courseDto);
            }
        }

        return courseDtos;
    }

    @Override
    public List<CourseDto> findFreeCourses() {
        // Free Course: Query theo price, nếu Price == 0 thì lấy ra
        List<Course> courses = courseRepository.findAll();
        List<CourseDto> courseDtos = new ArrayList<>();

        for (Course course : courses) {
            if (course.getStatus() == 1 && course.getPrice() == 0) {
                CourseDto courseDto = toCourseDto(course);
                courseDtos.add(courseDto);
            }
        }

        return courseDtos;
    }

    @Override
    public List<CourseDto> findRelatedCourses(long categoryId, long tagId) {
        // Related Course: Query theo category_id, hoặc nếu có tag thì query theo tag
        // nữa, này phải check condition nếu có thì lấy ra theo tag nữa kèm category_id
        boolean isExistTagId = tagId > 0;
        List<Course> courses = courseRepository.findAll();
        List<CourseDto> courseDtos = new ArrayList<>();

        Tag tag = isExistTagId ? tagRepository.findById(tagId).orElseThrow(
                () -> new NoSuchElementException("This tag with tagId: [" + tagId + "] is not exist.")) : new Tag();

        for (Course course : courses) {
            if (course.getStatus() == 1 && course.getCategory().getId() == categoryId) {
                if (course.getTags().contains(tag)) {
                    CourseDto courseDto = toCourseDto(course);
                    courseDtos.add(courseDto);
                    continue;
                }
                if (isExistTagId)
                    continue;

                CourseDto courseDto = toCourseDto(course);
                courseDtos.add(courseDto);
            }
        }

        return courseDtos;
    }

    @Override
    public List<CourseDto> findAll() {
        List<Course> courses = courseRepository.findAll();
        List<CourseDto> courseDtos = new ArrayList<>();
        for (Course course : courses) {
            if (course.getStatus() == 1) {
                CourseDto courseDto = toCourseDto(course);
                courseDtos.add(courseDto);
            }
        }
        return courseDtos;
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
                .setTags(splitTag(courseDto.getTagName(), course))
                .setAchievements(
                        splitAchievement(courseDto.getAchievementName(), course))
                .setLevel(courseDto.getLevel())
                .setStatus(courseDto.getStatus())
                .setImage(courseDto.getImage())
                .setSlug(Slugify.builder().build().slugify(courseDto.getName()))
                .setDuration(courseDto.getDuration())
                .setDescription(courseDto.getDescription())
                .setPrice(courseDto.getPrice())
                .setNet_price(courseDto.getNet_price());

        // courseRepository.save(course);

        // Proccessing Section

        List<String> sectionsStrings = courseDto.getSections();
        if (sectionsStrings != null) {
            Set<Section> sections = course.getSections();
            Set<Section> sectionFromString = new HashSet<>();
            // Create new set Section from List<String> sectionsStrings
            for (String sectionStr : sectionsStrings) {
                Section section = new Section();
                section.setName(sectionStr);
                section.setCourse(course);
                sectionFromString.add(section);
            }
            // Merge current set and set from list<String>
            sections.addAll(sectionFromString); // Will ignore Section existed.

            // Remove Section has been delete (Section not in List<String> sectionsStrings)
            Set<Section> tempSections = new HashSet<>();
            tempSections.addAll(sections);
            for (Section section : sections) {

                if (!sectionsStrings.contains(section.getName())) {
                    sectionRepository.deleteSectionsById(section.getId());
                    tempSections.remove(section);
                }
            }
            sections = tempSections;
            course.setSections(sections);
        }

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
    public Set<Tag> splitTag(String tag, Course course) {
        boolean isUpdatedCourse = course.getId() > 0 ? true : false;

        Set<Tag> newTags = new HashSet<>();
        List<Tag> allTags = tagRepository.findAll();
        List<Tag> tempAllTags = new ArrayList<>();
        tempAllTags.addAll(allTags);

        String[] tags = tag.split(",");
        List<String> list = Arrays.asList(tags);

        for (String tagName : tags) {
            // Error case 1: Add new tag and old tag in same String -> return only new Tag
            // Error case 2: No new tag -> return nothing
            Tag foundedTag = tagRepository.findTagByName(tagName);
            if (foundedTag == null) {
                Tag newTag = new Tag();
                newTag.setName(tagName);
                newTags.add(newTag);
                // Add new tag to temp
                tempAllTags.add(newTag);
            }
        }

        if (allTags.size() > 0) {
            for (Tag t : allTags) {
                if (!list.contains(t.getName())) {
                    tempAllTags.remove(t);
                }
            }
        }
        // To return with for create tag
        Set<Tag> returnTags = tempAllTags.stream().collect(Collectors.toSet());

        if (newTags.size() > 0) {
            // Only create new tag
            tagRepository.saveAll(newTags);
        }

        if (isUpdatedCourse) {
            var tagsOfCourse = course.getTags();
            Set<Tag> tempTag = new HashSet<>();
            tempTag.addAll(tagsOfCourse);

            for (Tag curreTag : tagsOfCourse) {
                if (!list.contains(curreTag.getName())) {
                    tempTag.remove(curreTag);
                }
            }

            tagsOfCourse = tempTag;
            if (newTags.size() > 0) {
                for (Tag newTag : newTags) {
                    tagsOfCourse.add(newTag);
                }
            }
            return tagsOfCourse;
        }

        return returnTags;
    }

    @Override
    public Set<Achievement> splitAchievement(String achievement, Course course) {

        boolean isUpdatedCourse = course.getId() > 0 ? true : false;

        // Check achievement if not exist add new.
        Set<Achievement> newAchievements = new HashSet<>();
        List<Achievement> allAchievements = achievementRepository.findAll();
        List<Achievement> tempAllAchievements = new ArrayList<>();
        tempAllAchievements.addAll(allAchievements);

        String[] achievements = achievement.split(",");
        List<String> list = Arrays.asList(achievements);
        for (String achievementName : achievements) {
            // Chỗ này nếu tạo course mới cùng tag sẽ mặc định tìm đc achievement -> null
            Achievement foundedAchievement = achievementRepository.findAchievementByName(achievementName);
            if (foundedAchievement == null) {
                Achievement newAchievement = new Achievement();
                newAchievement.setName(achievementName);
                newAchievements.add(newAchievement);
                // Add new achievement to temp
                tempAllAchievements.add(newAchievement);
            }
        }

        if (allAchievements.size() > 0) {
            for (Achievement a : allAchievements) {
                if (!list.contains(a.getName())) {
                    tempAllAchievements.remove(a);
                }
            }
        }
        // To return with for create tag
        Set<Achievement> returnAchievements = tempAllAchievements.stream().collect(Collectors.toSet());

        if (newAchievements.size() > 0) {
            // Only create new achievement
            achievementRepository.saveAll(newAchievements);
        }

        if (isUpdatedCourse) {
            var achievementOfCoure = course.getAchievements();

            Set<Achievement> tempAchievement = new HashSet<>();
            tempAchievement.addAll(achievementOfCoure);

            for (Achievement curreAchievement : achievementOfCoure) {
                if (!list.contains(curreAchievement.getName())) {
                    tempAchievement.remove(curreAchievement);
                }
            }

            achievementOfCoure = tempAchievement;
            if (newAchievements.size() > 0) {
                for (Achievement newAchievement : newAchievements) {
                    achievementOfCoure.add(newAchievement);
                }
            }
            return achievementOfCoure;
        }

        return returnAchievements;
    }

    @Override
    public BaseDto delete(long courseId) {
        try {
            Course course = courseRepository.findById(courseId).get();
            courseRepository.delete(course);
            return BaseDto.builder().type(AntType.success).message("Delete course successfully.")
                    .build();
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("This course with courseId: [" + courseId + "] is not exist.");
        } catch (Exception e) {
            throw new BadRequestException(BAD_REQUEST_EXCEPTION);
        }
    }

    private CourseDto toCourseDto(Course course) {
        List<String> achievementsList = course.getAchievements().stream()
                .map(achievement -> achievement.getName())
                .toList();
        List<String> tagsList = course.getTags().stream().map(tag -> tag.getName()).toList();
        new CourseDto();

        CourseDto courseDto = CourseDto.builder().id(course.getId())
                .name(course.getName())
                .price(course.getPrice())
                .net_price(course.getNet_price()).slug(course.getSlug())
                .image(course.getImage())
                .level(course.getLevel())
                .status(course.getStatus())
                .sections(course.getSections().stream()
                        .map(section -> section.getName())
                        .toList())
                .category(course.getCategory().getId())
                .category_name(course.getCategory().getName())
                .achievementName(String.join(",", achievementsList))
                .tagName(String.join(",", tagsList))
                .duration(course.getDuration()).build();

        return courseDto;
    }
}
