package com.aptech.coursemanagementserver.services;

import java.util.List;

import com.aptech.coursemanagementserver.dtos.VideoDto;
import com.aptech.coursemanagementserver.dtos.baseDto.BaseDto;
import com.aptech.coursemanagementserver.models.Video;

public interface VideoService {
    public Video findVideoByName(String videoName);

    public List<Video> findAll();

    public VideoDto findByLessonId(long lessonId);

    public BaseDto save(VideoDto video, long lessonId);

    public List<Video> saveAll(List<VideoDto> videos);

    public BaseDto delete(long sectionId);

}
