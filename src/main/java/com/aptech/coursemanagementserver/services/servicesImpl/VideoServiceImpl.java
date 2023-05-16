package com.aptech.coursemanagementserver.services.servicesImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.aptech.coursemanagementserver.dtos.VideoDto;
import com.aptech.coursemanagementserver.dtos.baseDto.BaseDto;
import com.aptech.coursemanagementserver.enums.AntType;
import com.aptech.coursemanagementserver.models.Lesson;
import com.aptech.coursemanagementserver.models.Video;
import com.aptech.coursemanagementserver.repositories.LessonRepository;
import com.aptech.coursemanagementserver.repositories.VideoRepository;
import com.aptech.coursemanagementserver.services.VideoService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VideoServiceImpl implements VideoService {
    private final VideoRepository videoRepository;
    private final LessonRepository lessonRepository;

    @Override
    public Video findVideoByName(String videoName) {
        return videoRepository.findVideoByName(videoName);
    }

    @Override
    public List<Video> findAll() {
        return videoRepository.findAll();
    }

    @Override
    public BaseDto save(VideoDto videoDto, long lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId).get();
        Video video = new Video();

        if (lesson == null) {
            return BaseDto.builder().type(AntType.error).message("This lesson does not exist.")
                    .build();
        }

        if (videoDto.getName().contains(lesson.getVideo().getName())) {
            return BaseDto.builder().type(AntType.error).message(videoDto.getName() + " is already existed.")
                    .build();
        }

        video.setLesson(lesson).setName(videoDto.getName()).setUrl(videoDto.getUrl());
        videoRepository.save(video);
        return BaseDto.builder().type(AntType.success).message("Create video successfully.")
                .build();
    }

    @Override
    public List<Video> saveAll(List<VideoDto> videosDto) {
        List<Video> videos = videosDto.stream().map(videoDto -> findVideoByName(videoDto.getName()))
                .collect(Collectors.toList());
        return videoRepository.saveAll(videos);
    }
}