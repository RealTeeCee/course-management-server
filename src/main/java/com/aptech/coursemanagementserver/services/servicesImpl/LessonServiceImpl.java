package com.aptech.coursemanagementserver.services.servicesImpl;

import static com.aptech.coursemanagementserver.constants.GlobalStorage.BAD_REQUEST_EXCEPTION;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.aptech.coursemanagementserver.dtos.LessonDto;
import com.aptech.coursemanagementserver.dtos.baseDto.BaseDto;
import com.aptech.coursemanagementserver.enums.AntType;
import com.aptech.coursemanagementserver.exceptions.BadRequestException;
import com.aptech.coursemanagementserver.models.Lesson;
import com.aptech.coursemanagementserver.models.Section;
import com.aptech.coursemanagementserver.models.Video;
import com.aptech.coursemanagementserver.repositories.LessonRepository;
import com.aptech.coursemanagementserver.repositories.SectionRepository;
import com.aptech.coursemanagementserver.services.LessonService;
import com.aptech.coursemanagementserver.services.authServices.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {

    private final LessonRepository lessonRepository;
    private final SectionRepository sectionRepository;
    private final UserService userService;

    @Override
    public LessonDto findById(long lessonId) {
        try {
            Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(
                    () -> new NoSuchElementException("The lesson with lessonId: [" + lessonId + "] is not exist."));
            LessonDto lessonDto = new LessonDto();
            lessonDto.setId(lessonId);
            lessonDto.setName(lesson.getName());
            lessonDto.setDescription(lesson.getDescription());
            lessonDto.setDuration(lesson.getDuration());
            lessonDto.setStatus(lesson.getStatus());
            lessonDto.setOrdered(lesson.getOrdered());
            lessonDto.setSectionId(lesson.getSection().getId());

            return lessonDto;
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(e.getMessage());

        } catch (Exception e) {
            throw new BadRequestException(BAD_REQUEST_EXCEPTION);
        }

    }

    @Override
    public Lesson findLessonByName(String lessonName) {
        return lessonRepository.findLessonByName(lessonName);
    }

    @Override
    public List<Lesson> findAll() {
        return lessonRepository.findAll();
    }

    @Override
    public List<LessonDto> findAllBySectionId(long sectionId) {
        try {
            Section section = sectionRepository.findById(sectionId).orElseThrow(() -> new NoSuchElementException(
                    "The section with sectionId: [" + sectionId + "] is not exist."));

            List<LessonDto> lessonDtos = new ArrayList<>();
            List<Lesson> lessons = section.getLessons().stream()
                    .sorted(Comparator.comparing(Lesson::getOrdered)
                            .thenComparing(Comparator.comparing(Lesson::getCreated_at).reversed()))
                    .collect(Collectors.toList());
            if (userService.checkIsUser()) {
                lessons = lessons.stream().filter(sec -> sec.getStatus() == 1).toList();
            }
            for (Lesson lesson : lessons) {

                LessonDto lessonDto = LessonDto.builder()
                        .id(lesson.getId())
                        .name(lesson.getName())
                        .description(lesson.getDescription())
                        .duration(lesson.getDuration())
                        .status(lesson.getStatus())
                        .ordered(lesson.getOrdered())
                        .sectionId(sectionId).build();

                lessonDtos.add(lessonDto);

            }
            return lessonDtos;

        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException(BAD_REQUEST_EXCEPTION);
        }

    }

    @Override
    public BaseDto save(LessonDto lessonDto) {
        try {
            Lesson lesson = new Lesson();
            // Video video = new Video();
            lesson.setDescription(lessonDto.getDescription()).setDuration(lessonDto.getDuration())
                    .setStatus(lessonDto.getStatus())
                    .setName(lessonDto.getName())
                    .setSection(sectionRepository.findById(lessonDto.getSectionId()).get())
                    // .setVideo(video)
                    .setOrdered(lessonDto.getOrdered());
            // video.setLesson(lesson);
            lessonRepository.save(lesson);
            return BaseDto.builder().type(AntType.success).message("Create lesson successfully.").build();
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(
                    "The section with sectionId: [" + lessonDto.getSectionId() + "] is not exist.");
        } catch (Exception e) {
            throw new BadRequestException(BAD_REQUEST_EXCEPTION);
        }

    }

    @Override
    public BaseDto updateLesson(LessonDto lessonDto) {
        try {
            Section section = sectionRepository.findById(lessonDto.getSectionId())
                    .orElseThrow(() -> new NoSuchElementException(
                            "The section with sectionId: [" + lessonDto.getSectionId() + "] is not exist."));

            Lesson lesson = lessonRepository.findById(lessonDto.getId()).orElseThrow(() -> new NoSuchElementException(
                    "The lesson with lessonId: [" + lessonDto.getId() + "] is not exist."));

            lesson.setName(lessonDto.getName()).setDescription(lessonDto.getDescription())
                    .setDuration(lessonDto.getDuration())
                    .setStatus(lessonDto.getStatus())
                    .setOrdered(lessonDto.getOrdered())
                    .setSection(section);

            lessonRepository.save(lesson);

            return BaseDto.builder().type(AntType.success).message("Update lesson successfully.").build();
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException(BAD_REQUEST_EXCEPTION);
        }
    }

    @Override
    public BaseDto delete(long lessonId) {
        try {
            Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(
                    () -> new NoSuchElementException("The lesson with lessonId: [" + lessonId + "] is not exist."));
            lessonRepository.delete(lesson);
            return BaseDto.builder().type(AntType.success).message("Delete lesson successfully.")
                    .build();
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException(BAD_REQUEST_EXCEPTION);
        }
    }
}
