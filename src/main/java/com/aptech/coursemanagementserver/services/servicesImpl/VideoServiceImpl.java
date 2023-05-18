package com.aptech.coursemanagementserver.services.servicesImpl;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.aptech.coursemanagementserver.dtos.VideoDto;
import com.aptech.coursemanagementserver.dtos.baseDto.BaseDto;
import com.aptech.coursemanagementserver.enums.AntType;
import com.aptech.coursemanagementserver.exceptions.BadRequestException;
import com.aptech.coursemanagementserver.models.Lesson;
import com.aptech.coursemanagementserver.models.Video;
import com.aptech.coursemanagementserver.repositories.LessonRepository;
import com.aptech.coursemanagementserver.repositories.VideoRepository;
import com.aptech.coursemanagementserver.services.VideoService;
import static com.aptech.coursemanagementserver.constants.GlobalStorage.BAD_REQUEST_EXCEPTION;

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
    public VideoDto findByLessonId(long lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId).get();
        Video video = lesson.getVideo();

        VideoDto videoDto = VideoDto.builder().name(video.getName()).url(video.getUrl()).lessonId(lessonId).build();

        return videoDto;
    }

    @Override
    public BaseDto save(VideoDto videoDto, long lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId).get();
        Video video = new Video();

        if (lesson == null) {
            throw new BadRequestException("This lesson with id: [" + lessonId + "]does not exist.");
        }

        if (videoDto.getName().contains(lesson.getVideo().getName())) {
            throw new BadRequestException(videoDto.getName() + " is already existed.");
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

    @Override
    public BaseDto delete(long videoId) {
        try {
            Video video = videoRepository.findById(videoId).get();
            videoRepository.delete(video);
            return BaseDto.builder().type(AntType.success).message("Delete video successfully.")
                    .build();
        } catch (NoSuchElementException e) {
            throw new BadRequestException("This video with videoId: [" + videoId + "] is not exist.");

        } catch (Exception e) {
            throw new BadRequestException(BAD_REQUEST_EXCEPTION);
        }
    }

    @Override
    public BaseDto update(VideoDto videoDto, long videoId) {
        try {
            Video video = videoRepository.findById(videoId).get();
            video.setUrl(videoDto.getUrl()).setName(videoDto.getName());

            return BaseDto.builder().type(AntType.success).message("Update video successfully.")
                    .build();
        } catch (NoSuchElementException e) {
            throw new BadRequestException("This video with videoId: [" + videoId + "] is not exist.");

        } catch (Exception e) {
            throw new BadRequestException(BAD_REQUEST_EXCEPTION);
        }
    }
}