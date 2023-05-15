package com.aptech.coursemanagementserver.services;

import java.util.List;

import com.aptech.coursemanagementserver.dtos.VideoDto;
import com.aptech.coursemanagementserver.models.Video;

public interface VideoService {
    public Video findVideoByName(String videoName);

    public List<Video> findAll();

    public Video save(VideoDto video);

    public List<Video> saveAll(List<VideoDto> videos);
}
