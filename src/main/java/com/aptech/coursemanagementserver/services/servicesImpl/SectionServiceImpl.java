package com.aptech.coursemanagementserver.services.servicesImpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.aptech.coursemanagementserver.dtos.SectionDto;
import com.aptech.coursemanagementserver.dtos.baseDto.BaseDto;
import com.aptech.coursemanagementserver.enums.AntType;
import com.aptech.coursemanagementserver.exceptions.BadRequestException;
import com.aptech.coursemanagementserver.models.Course;
import com.aptech.coursemanagementserver.models.Section;
import com.aptech.coursemanagementserver.repositories.CourseRepository;
import com.aptech.coursemanagementserver.repositories.SectionRepository;
import com.aptech.coursemanagementserver.services.SectionService;
import static com.aptech.coursemanagementserver.constants.GlobalStorage.BAD_REQUEST_EXCEPTION;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SectionServiceImpl implements SectionService {
    private final CourseRepository courseRepository;
    private final SectionRepository sectionRepository;

    @Override
    public Section findSectionByName(String sectionName) {
        return sectionRepository.findSectionByName(sectionName);
    }

    @Override
    public List<Section> findAllByCourseId(long courseId) {
        return sectionRepository.findAllByCourseId(courseId);
    }

    @Override
    public SectionDto findAllNameByCourseId(long courseId) {
        Course course = courseRepository.findById(courseId).get();
        SectionDto sectionDto = new SectionDto();
        List<String> list = new ArrayList<>();

        for (Section section : course.getSections()) {
            list.add(section.getName());
        }

        sectionDto.setCourseId(courseId);
        sectionDto.setSections(list);

        return sectionDto;
    }

    @Override
    public List<Section> findAll() {
        return sectionRepository.findAll();
    }

    @Override
    public BaseDto saveSectionsToCourse(SectionDto sectionDto, long courseId) {
        try {
            Course course = courseRepository.findById(courseId).get();

            List<String> sectionsString = sectionDto.getSections();
            Set<Section> sections = new HashSet<>();

            List<Section> sectionsInCourse = findAllByCourseId(courseId);

            if (sectionsInCourse.size() > 0) {
                for (Section s : sectionsInCourse) {
                    if (sectionDto.getSections().contains(s.getName())) {
                        return BaseDto.builder().type(AntType.error).message(s.getName() + " is already existed.")
                                .build();
                    }

                    Section section = new Section();
                    section.setName(s.getName());
                    section.setCourse(s.getCourse());
                    sections.add(section);
                }
            }

            Optional<Section> sectionCourse = sections.stream().findFirst();

            if (sectionsString != null) {
                for (String sectionString : sectionsString) {
                    Section section = new Section();
                    section.setName(sectionString);
                    section.setCourse(sectionCourse.isPresent() ? sectionCourse.get().getCourse()
                            : course);
                    sections.add(section);
                }
            }

            sectionRepository.saveAll(sections);
            // course.setSections(courseDto.getSections());
            // course.setSections(sections.stream().collect(Collectors.toSet()));
            // courseRepository.save(course);
            return BaseDto.builder().type(AntType.success).message("Create section successfully.").build();

        } catch (NoSuchElementException e) {
            throw new BadRequestException("This course with courseId: [" + courseId + "] is not exist.");

        } catch (Exception e) {
            throw new BadRequestException(BAD_REQUEST_EXCEPTION);
        }
    }

    @Override
    public BaseDto updateSection(SectionDto sectionDto, long sectionId) {
        try {
            Section section = sectionRepository.findById(sectionId).get();
            section.setName(sectionDto.getName());

            return BaseDto.builder().type(AntType.success).message("Update section successfully.").build();
        } catch (NoSuchElementException e) {
            throw new BadRequestException("This section with sectionId: [" + sectionId + "] is not exist.");

        } catch (Exception e) {
            throw new BadRequestException(BAD_REQUEST_EXCEPTION);
        }
    }

    @Override
    public BaseDto delete(long sectionId) {
        try {
            Section section = sectionRepository.findById(sectionId).get();
            sectionRepository.delete(section);
            return BaseDto.builder().type(AntType.success).message("Delete section successfully.")
                    .build();
        } catch (NoSuchElementException e) {
            throw new BadRequestException("This section with sectionId: [" + sectionId + "] is not exist.");

        } catch (Exception e) {
            throw new BadRequestException(BAD_REQUEST_EXCEPTION);
        }
    }
}
