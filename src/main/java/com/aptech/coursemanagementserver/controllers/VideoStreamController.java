package com.aptech.coursemanagementserver.controllers;

import java.io.IOException;
import java.nio.file.Paths;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.aptech.coursemanagementserver.services.VideoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import static com.aptech.coursemanagementserver.constants.GlobalStorage.VIDEO;

@RestController
@RequestMapping("/video")
@RequiredArgsConstructor
@Tag(name = "Video Stream Endpoints")
public class VideoStreamController {

    private final VideoService videoStreamService;

    // @GetMapping("/stream/{fileType}/{fileName}")
    // @Operation(summary = "[ANYROLE] - Partial serving video content")
    // public Mono<ResponseEntity<byte[]>> streamVideo(
    // @RequestHeader(value = "Range", required = false) String httpRangeList,
    // @PathVariable("fileType") String fileType,
    // @PathVariable("fileName") String fileName) {
    // return Mono.just(videoStreamService.prepareVideoContent(fileName, fileType,
    // httpRangeList));
    // }
    @GetMapping("/stream/{fileType}/{fileName}")
    @Operation(summary = "[ANYROLE] - Partial serving video content")
    public Mono<ResponseEntity<StreamingResponseBody>> streamVideo(
            @RequestHeader(value = "Range", required = false) String httpRangeList,
            @PathVariable("fileType") String fileType,
            @PathVariable("fileName") String fileName) throws IOException {
        String range = httpRangeList == null ? "1048576-" : httpRangeList;
        String filePathString = Paths.get(VIDEO, fileName + "." + fileType).toString();
        return Mono.just(videoStreamService.loadPartialMediaFile(filePathString,
                range));
    }

    @GetMapping("/caption/{fileName}")
    @Operation(summary = "[ANYROLE] - Get Caption of Video")
    public Mono<ResponseEntity<byte[]>> getCaption(

            @PathVariable("fileName") String fileName) {
        return Mono.just(videoStreamService.prepareCaptionContent(fileName));
    }
}
