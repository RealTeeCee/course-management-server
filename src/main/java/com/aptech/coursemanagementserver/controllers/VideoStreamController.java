package com.aptech.coursemanagementserver.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aptech.coursemanagementserver.services.VideoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/video")
@RequiredArgsConstructor
@Tag(name = "Video Stream Endpoints")
public class VideoStreamController {

    private final VideoService videoStreamService;

    @GetMapping("/stream/{fileType}/{fileName}")
    @Operation(summary = "[ANYROLE] - Partial serving video content")
    public ResponseEntity<byte[]> streamVideo(
            @RequestHeader(value = "Range", required = false) String httpRangeList,
            @PathVariable("fileType") String fileType,
            @PathVariable("fileName") String fileName) {
        return videoStreamService.prepareVideoContent(fileName, fileType, httpRangeList);
    }

    @GetMapping("/caption/{fileName}")
    @Operation(summary = "[ANYROLE] - Get Caption of Video")
    public ResponseEntity<byte[]> getCaption(

            @PathVariable("fileName") String fileName) {
        return videoStreamService.prepareCaptionContent(fileName);
    }
}
