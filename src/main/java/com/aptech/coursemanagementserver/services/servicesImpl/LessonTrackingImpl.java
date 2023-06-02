package com.aptech.coursemanagementserver.services.servicesImpl;

import static com.aptech.coursemanagementserver.constants.GlobalStorage.BAD_REQUEST_EXCEPTION;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import com.aptech.coursemanagementserver.dtos.LearningDto;
import com.aptech.coursemanagementserver.dtos.LessonDto;
import com.aptech.coursemanagementserver.dtos.LessonTrackingDto;
import com.aptech.coursemanagementserver.dtos.SectionDto;
import com.aptech.coursemanagementserver.dtos.VideoDto;
import com.aptech.coursemanagementserver.exceptions.BadRequestException;
import com.aptech.coursemanagementserver.models.Enrollment;
import com.aptech.coursemanagementserver.models.Lesson;
import com.aptech.coursemanagementserver.models.LessonTracking;
import com.aptech.coursemanagementserver.models.LessonTrackingId;
import com.aptech.coursemanagementserver.models.Video;
import com.aptech.coursemanagementserver.repositories.EnrollmentRepository;
import com.aptech.coursemanagementserver.repositories.LessonRepository;
import com.aptech.coursemanagementserver.repositories.LessonTrackingRepository;
import com.aptech.coursemanagementserver.repositories.VideoRepository;
import com.aptech.coursemanagementserver.services.LessonTrackingService;
import com.aptech.coursemanagementserver.services.SectionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LessonTrackingImpl implements LessonTrackingService {
    private final LessonTrackingRepository lessonTrackingRepository;
    private final LessonRepository lessonRepository;
    private final VideoRepository videoRepository;
    private final SectionService sectionService;
    private final EnrollmentRepository enrollmentRepository;

    @Override
    public LessonTrackingDto loadTrack(LessonTrackingDto lessonTrackingDto) {
        try {
            LessonTracking lessonTracking = lessonTrackingRepository.findTrackedByEnrollmentIdAndCourseId(
                    lessonTrackingDto.getEnrollmentId(), lessonTrackingDto.getCourseId());
            LessonTrackingDto returnTrackingDto = LessonTrackingDto.builder()
                    .enrollmentId(lessonTracking.getTrackId().getEnrollment_id())
                    .courseId(lessonTracking.getTrackId().getCourse_id())
                    .sectionId(lessonTracking.getTrackId().getSection_id())
                    .lessonId(lessonTracking.getTrackId().getSection_id())
                    .lessonId(lessonTracking.getTrackId().getLession_id())
                    .videoId(lessonTracking.getTrackId().getVideo_id())
                    .isCompleted(lessonTracking.isCompleted())
                    .isTracked(lessonTracking.isTracked())
                    .resumePoint(lessonTracking.getResumePoint()).build();
            return returnTrackingDto;

        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException(BAD_REQUEST_EXCEPTION);
        }
    }

    @Override
    public boolean saveTrack(LessonTrackingDto lessonTrackingDto) {
        try {
            LessonTrackingId trackId = setTrackId(lessonTrackingDto);
            boolean isUpdated = lessonTrackingRepository.findByTrackId(trackId).isPresent();
            addTrack(lessonTrackingDto, isUpdated);

            return true;
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException(BAD_REQUEST_EXCEPTION);
        }
    }

    @Override
    public boolean complete(LessonTrackingDto lessonTrackingDto) {
        try {
            LessonTrackingId trackId = setTrackId(lessonTrackingDto);
            LessonTracking lessonTracking = lessonTrackingRepository.findByTrackId(trackId).orElseThrow(
                    () -> new NoSuchElementException(
                            "The track with trackId:[" + trackId.toString() + "] is not exist."));
            lessonTracking.setCompleted(true);
            lessonTrackingRepository.save(lessonTracking);
            return true;
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException(BAD_REQUEST_EXCEPTION);
        }
    }

    @Override
    public boolean updateProgress(long enrollmentId, long courseId) {
        try {
            Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                    .orElseThrow(() -> new NoSuchElementException(
                            "The enrollment with enrollmentId:[" + enrollmentId + "] is not exist."));

            List<LessonTracking> completeTracks = lessonTrackingRepository
                    .findAllCompletedByEnrollmentIdAndCourseId(enrollmentId, courseId);
            List<Lesson> lessonsInCourse = lessonRepository.findAllByCourseId(courseId);

            long progress = (completeTracks.size() / lessonsInCourse.size()) * 100;
            enrollment.setProgress(progress);
            enrollmentRepository.save(enrollment);

            return true;
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException(BAD_REQUEST_EXCEPTION);
        }
    }

    @Override
    public LearningDto getLearnDetails(long courseId) {
        try {
            LearningDto learningDto = new LearningDto();
            List<SectionDto> sectionDtos = sectionService.findAllByCourseId(courseId);
            learningDto.setSectionDto(sectionDtos);

            List<Lesson> lessons = lessonRepository.findAllByCourseId(courseId);
            for (Lesson lesson : lessons) {
                LessonDto lessonDto = LessonDto.builder().description(lesson.getDescription())
                        .duration(lesson.getDuration()).id(lesson.getId()).name(lesson.getName())
                        .sectionId(lesson.getSection().getId()).status(lesson.getStatus()).build();
                learningDto.getLessonDto().add(lessonDto);
            }

            List<Video> videos = videoRepository.findAllByCourseId(courseId);
            for (Video video : videos) {
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
                        .captionData(map).lessonId(video.getLesson().getId()).build();

                learningDto.getVideoDto().add(videoDto);
            }
            return learningDto;
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException(BAD_REQUEST_EXCEPTION);
        }
    }

    private void addTrack(LessonTrackingDto lessonTrackingDto, boolean isUpdated) {
        LessonTrackingId trackId = setTrackId(lessonTrackingDto);

        LessonTracking lessonTracking = isUpdated ? lessonTrackingRepository.findByTrackId(trackId).orElseThrow(
                () -> new NoSuchElementException(
                        "The track with trackId:[" + trackId.toString() + "] is not exist."))
                : new LessonTracking();

        LessonTracking track = lessonTrackingRepository
                .findTrackedByEnrollmentIdAndCourseId(lessonTrackingDto.getEnrollmentId(),
                        lessonTrackingDto.getCourseId());

        if (track != null) {
            track.setTracked(false);
            lessonTrackingRepository.save(track);
        }

        if (isUpdated) {
            lessonTracking.setTrackId(trackId).setTracked(true)
                    .setResumePoint(lessonTrackingDto.getResumePoint());
        } else {
            lessonTracking.setTrackId(trackId).setCompleted(false).setTracked(true)
                    .setResumePoint(lessonTrackingDto.getResumePoint());
        }

        lessonTrackingRepository.save(lessonTracking);
    }

    private LessonTrackingId setTrackId(LessonTrackingDto lessonTrackingDto) {
        LessonTrackingId trackId = new LessonTrackingId();

        trackId.setEnrollment_id(lessonTrackingDto.getEnrollmentId())
                .setCourse_id(lessonTrackingDto.getCourseId())
                .setSection_id(lessonTrackingDto.getSectionId())
                .setLession_id(lessonTrackingDto.getLessonId())
                .setVideo_id(lessonTrackingDto.getVideoId());
        return trackId;
    }

}