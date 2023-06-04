package com.aptech.coursemanagementserver.services.servicesImpl;

import static com.aptech.coursemanagementserver.constants.GlobalStorage.ACCEPT_RANGES;
import static com.aptech.coursemanagementserver.constants.GlobalStorage.BAD_REQUEST_EXCEPTION;
import static com.aptech.coursemanagementserver.constants.GlobalStorage.BYTES;
import static com.aptech.coursemanagementserver.constants.GlobalStorage.BYTE_RANGE;
import static com.aptech.coursemanagementserver.constants.GlobalStorage.CAPTION;
import static com.aptech.coursemanagementserver.constants.GlobalStorage.CHUNK_SIZE;
import static com.aptech.coursemanagementserver.constants.GlobalStorage.CONTENT_LENGTH;
import static com.aptech.coursemanagementserver.constants.GlobalStorage.CONTENT_RANGE;
import static com.aptech.coursemanagementserver.constants.GlobalStorage.CONTENT_TYPE;
import static com.aptech.coursemanagementserver.constants.GlobalStorage.VIDEO;
import static com.aptech.coursemanagementserver.constants.GlobalStorage.VIDEO_CONTENT;
import static com.aptech.coursemanagementserver.constants.GlobalStorage.VTT_CONTENT;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.aptech.coursemanagementserver.dtos.VideoDto;
import com.aptech.coursemanagementserver.dtos.baseDto.BaseDto;
import com.aptech.coursemanagementserver.enums.AntType;
import com.aptech.coursemanagementserver.exceptions.BadRequestException;
import com.aptech.coursemanagementserver.exceptions.IsExistedException;
import com.aptech.coursemanagementserver.models.Lesson;
import com.aptech.coursemanagementserver.models.Video;
import com.aptech.coursemanagementserver.repositories.LessonRepository;
import com.aptech.coursemanagementserver.repositories.VideoRepository;
import com.aptech.coursemanagementserver.services.VideoService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
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
        try {
            Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(
                    () -> new NoSuchElementException("The lesson with lessonId: [" + lessonId + "] is not exist."));
            Video video = lesson.getVideo();

            String[] parts = video.getCaptionUrls().split(",");
            Map<String, String> map = new HashMap<>();
            for (String part : parts) {
                int lastDotIndex = part.lastIndexOf(".");
                if (lastDotIndex > 0) {
                    String langCode = part.substring(lastDotIndex - 2, lastDotIndex);
                    map.put(langCode, part);
                }
            }
            // ObjectMapper objectMapper = new ObjectMapper();
            // String captionData = objectMapper.writeValueAsString(map);

            VideoDto videoDto = VideoDto.builder().id(video.getId()).name(video.getName()).url(video.getUrl())
                    .status(video.getStatus())
                    .captionData(map).lessonId(lessonId).build();

            return videoDto;
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException(BAD_REQUEST_EXCEPTION);
        }

    }

    @Override
    public BaseDto save(VideoDto videoDto, long lessonId) {
        try {
            Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(
                    () -> new NoSuchElementException("The lesson with id: [" + lessonId + "]does not exist."));

            if (findVideoByName(videoDto.getName()) != null) {
                throw new IsExistedException(videoDto.getName());
            }

            Video videoOfLesson = lesson.getVideo();
            Video video = videoOfLesson == null ? new Video() : videoOfLesson;
            video.setName(videoDto.getName()).setUrl(videoDto.getUrl())
                    .setCaptionUrls(String.join(",", videoDto.getCaptionUrls()));
            if (videoOfLesson == null) {
                video.setLesson(lesson);
            }

            videoRepository.save(video);

            return BaseDto.builder().type(AntType.success).message("Create video successfully.")
                    .build();
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(e.getMessage());
        } catch (IsExistedException e) {
            throw new BadRequestException(e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException(BAD_REQUEST_EXCEPTION);
        }

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
            Video video = videoRepository.findById(videoId).orElseThrow(
                    () -> new NoSuchElementException("The video with videoId: [" + videoId + "] is not exist."));
            videoRepository.delete(video);
            return BaseDto.builder().type(AntType.success).message("Delete video successfully.")
                    .build();
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(e.getMessage());

        } catch (Exception e) {
            throw new BadRequestException(BAD_REQUEST_EXCEPTION);
        }
    }

    @Override
    public BaseDto update(VideoDto videoDto) {
        try {
            Video video = videoRepository.findById(videoDto.getId()).get();
            video.setUrl(videoDto.getUrl()).setName(videoDto.getName());

            return BaseDto.builder().type(AntType.success).message("Update video successfully.")
                    .build();
        } catch (NoSuchElementException e) {
            throw new BadRequestException("The video with videoId: [" + videoDto.getId() + "] is not exist.");

        } catch (Exception e) {
            throw new BadRequestException(BAD_REQUEST_EXCEPTION);
        }
    }

    @Override
    public ResponseEntity<byte[]> prepareVideoContent(String fileName, String fileType, String range) {
        try {
            final String fileKey = fileName + "." + fileType;
            long rangeStart = 0;
            long rangeEnd = CHUNK_SIZE;
            final Long fileSize = getVideoSize(fileKey);
            if (range == null) {
                return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                        .header(CONTENT_TYPE, VIDEO_CONTENT + fileType)
                        .header(ACCEPT_RANGES, BYTES)
                        .header(CONTENT_LENGTH, String.valueOf(rangeEnd))
                        .header(CONTENT_RANGE, BYTES + " " + rangeStart + "-" + rangeEnd + "/" + fileSize)
                        .header(CONTENT_LENGTH, String.valueOf(fileSize))
                        .body(readByteRangeNew(fileKey, rangeStart, rangeEnd)); // Read the object and convert it as
                                                                                // bytes
            }
            String[] ranges = range.split("-");
            rangeStart = Long.parseLong(ranges[0].substring(6));
            if (ranges.length > 1) {
                rangeEnd = Long.parseLong(ranges[1]);
            } else {
                rangeEnd = rangeStart + CHUNK_SIZE;
            }

            rangeEnd = Math.min(rangeEnd, fileSize - 1);
            final byte[] data = readByteRangeNew(fileKey, rangeStart, rangeEnd);
            final String contentLength = String.valueOf((rangeEnd - rangeStart) + 1);
            HttpStatus httpStatus = HttpStatus.PARTIAL_CONTENT;
            if (rangeEnd >= fileSize) {
                httpStatus = HttpStatus.OK;
            }
            return ResponseEntity.status(httpStatus)
                    .header(CONTENT_TYPE, VIDEO_CONTENT + fileType)
                    .header(ACCEPT_RANGES, BYTES)
                    .header(CONTENT_LENGTH, contentLength)
                    .header(CONTENT_RANGE, BYTES + " " + rangeStart + "-" + rangeEnd + "/" + fileSize)
                    .body(data);
        } catch (IOException e) {
            log.error("Exception while reading the file {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<byte[]> prepareCaptionContent(final String fileName) {
        try {
            Path path = Paths.get(CAPTION, fileName);
            byte[] data = Files.readAllBytes(path);

            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.parseMediaType(VTT_CONTENT))
                    .body(data);
        } catch (IOException e) {
            log.error("Exception while reading the file {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    public byte[] readByteRangeNew(String filename, long start, long end) throws IOException {
        Path path = Paths.get(VIDEO, filename);
        byte[] data = Files.readAllBytes(path);
        byte[] result = new byte[(int) (end - start) + 1];
        System.arraycopy(data, (int) start, result, 0, (int) (end - start) + 1);
        return result;
    }

    public byte[] readByteRange(String filename, long start, long end) throws IOException {
        Path path = Paths.get(VIDEO, filename);
        try (InputStream inputStream = (Files.newInputStream(path));
                ByteArrayOutputStream bufferedOutputStream = new ByteArrayOutputStream()) {
            byte[] data = new byte[BYTE_RANGE];
            int nRead;
            while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                bufferedOutputStream.write(data, 0, nRead);
            }
            bufferedOutputStream.flush();
            byte[] result = new byte[(int) (end - start) + 1];
            System.arraycopy(bufferedOutputStream.toByteArray(), (int) start, result, 0, result.length);
            return result;
        }
    }

    private String getVideoPath() {
        URL url = this.getClass().getClassLoader().getResource(VIDEO);
        assert url != null;
        return new File(url.getFile()).getAbsolutePath();
    }

    private String getCaptionPath() {
        URL url = this.getClass().getClassLoader().getResource(CAPTION);
        assert url != null;
        return new File(url.getFile()).getAbsolutePath();
    }

    /*
     * public Long getVideoSize(String fileName) {
     * return Optional.ofNullable(fileName)
     * .map(file -> Paths.get(getVideoPath(), file))
     * .map(this::sizeFromFile)
     * .orElse(0L);
     * }
     */
    public Long getVideoSize(String fileName) throws IOException {

        return Files.size((Paths.get(VIDEO, fileName)));
        // return Optional.ofNullable(fileName)
        // .map(file -> Paths.get(VIDEO, file))
        // .map(this::sizeFromFile)
        // .orElse(0L);
    }

    private Long sizeFromFile(Path path) {
        try {
            return Files.size(path);
        } catch (IOException e) {
            log.error("Error while getting the file size: {}", e.getMessage());
        }
        return 0L;
    }
}