package com.aptech.coursemanagementserver.services.servicesImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.aptech.coursemanagementserver.dtos.VideoDto;
import com.aptech.coursemanagementserver.models.Video;
import com.aptech.coursemanagementserver.repositories.VideoRepository;
import com.aptech.coursemanagementserver.services.VideoService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VideoServiceImpl implements VideoService {
    private final VideoRepository videoRepository;

    @Override
    public Video findVideoByName(String videoName) {
        return videoRepository.findVideoByName(videoName);
    }

    @Override
    public List<Video> findAll() {
        return videoRepository.findAll();
    }

    @Override
    public Video save(VideoDto videoDto) {
        Video video = videoRepository.findVideoByName(videoDto.getName());
        return videoRepository.save(video);
    }

    @Override
    public List<Video> saveAll(List<VideoDto> videosDto) {
        List<Video> videos = videosDto.stream().map(videoDto -> findVideoByName(videoDto.getName()))
                .collect(Collectors.toList());
        return videoRepository.saveAll(videos);
    }
}