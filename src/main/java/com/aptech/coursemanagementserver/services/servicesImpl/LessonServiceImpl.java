package com.aptech.coursemanagementserver.services.servicesImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

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
import static com.aptech.coursemanagementserver.constants.GlobalStorage.BAD_REQUEST_EXCEPTION;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {
    private final LessonRepository lessonRepository;
    private final SectionRepository sectionRepository;

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
        Section section = sectionRepository.findById(sectionId).get();
        List<LessonDto> lessonDtos = new ArrayList<>();

        for (Lesson lesson : section.getLessons()) {
            LessonDto lessonDto = LessonDto.builder().name(lesson.getName()).description(lesson.getDescription())
                    .duration(lesson.getDuration()).sectionId(sectionId).build();
            lessonDtos.add(lessonDto);
        }

        return lessonDtos;
    }

    @Override
    public BaseDto saveLessonsToSection(LessonDto lessonDto, long sectionId) {
        Lesson lesson = new Lesson();
        Section section = sectionRepository.findById(sectionId).get();

        if (section == null) {
            throw new BadRequestException("This section with id: [" + sectionId + "]does not exist.");

        }

        for (Lesson l : section.getLessons()) {
            if (lessonDto.getName().contains(l.getName())) {
                return BaseDto.builder().type(AntType.error).message(lessonDto.getName() + " is already existed.")
                        .build();
            }
        }

        Video video = new Video();
        lesson.setName(lessonDto.getName()).setDescription(lessonDto.getDescription())
                .setDuration(lessonDto.getDuration()).setSection(section).setVideo(video);

        video.setLesson(lesson);

        lessonRepository.save(lesson);
        return BaseDto.builder().type(AntType.success).message("Create lesson successfully.").build();
    }

    @Override
    public BaseDto updateLesson(LessonDto lessonDto, long lessonId) {
        try {
            Lesson lesson = lessonRepository.findById(lessonId).get();
            lesson.setName(lessonDto.getName()).setDescription(lessonDto.getDescription())
                    .setDuration(lessonDto.getDuration());

            return BaseDto.builder().type(AntType.success).message("Update lesson successfully.").build();
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("This lesson with lessonId: [" + lessonId + "] is not exist.");
        } catch (Exception e) {
            throw new BadRequestException(BAD_REQUEST_EXCEPTION);
        }
    }

    @Override
    public BaseDto delete(long lessonId) {
        try {
            Lesson lesson = lessonRepository.findById(lessonId).get();
            lessonRepository.delete(lesson);
            return BaseDto.builder().type(AntType.success).message("Delete lesson successfully.")
                    .build();
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("This lesson with lessonId: [" + lessonId + "] is not exist.");
        } catch (Exception e) {
            throw new BadRequestException(BAD_REQUEST_EXCEPTION);
        }
    }
}
