package com.aptech.coursemanagementserver.services.servicesImpl;

import static com.aptech.coursemanagementserver.constants.GlobalStorage.BAD_REQUEST_EXCEPTION;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import com.aptech.coursemanagementserver.dtos.LessonTrackingDto;
import com.aptech.coursemanagementserver.exceptions.BadRequestException;
import com.aptech.coursemanagementserver.models.Enrollment;
import com.aptech.coursemanagementserver.models.Lesson;
import com.aptech.coursemanagementserver.models.LessonTracking;
import com.aptech.coursemanagementserver.models.LessonTrackingId;
import com.aptech.coursemanagementserver.repositories.EnrollmentRepository;
import com.aptech.coursemanagementserver.repositories.LessonRepository;
import com.aptech.coursemanagementserver.repositories.LessonTrackingRepository;
import com.aptech.coursemanagementserver.services.LessonTrackingService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LessonTrackingImpl implements LessonTrackingService {
    private final LessonTrackingRepository lessonTrackingRepository;
    private final LessonRepository lessonRepository;
    private final EnrollmentRepository enrollmentRepository;

    @Override
    public LessonTrackingDto loadTrack(LessonTrackingDto lessonTrackingDto) {
        try {
            LessonTrackingId trackId = setTrackId(lessonTrackingDto);
            LessonTracking lessonTracking = lessonTrackingRepository.findByTrackId(trackId).orElseThrow(
                    () -> new NoSuchElementException(
                            "The track with trackId:[" + trackId.toString() + "] is not exist."));

            LessonTrackingDto returnTrackingDto = LessonTrackingDto.builder()
                    .enrollmentId(lessonTracking.getTrackId().getEnrollment_id())
                    .courseId(lessonTracking.getTrackId().getCourse_id())
                    .sectionId(lessonTracking.getTrackId().getSection_id())
                    .lessonId(lessonTracking.getTrackId().getSection_id())
                    .lessonId(lessonTracking.getTrackId().getLession_id())
                    .videoId(lessonTracking.getTrackId().getVideo_id()).isCompleted(lessonTracking.isCompleted())
                    .isTracked(lessonTracking.isTracked()).resumePoint(lessonTracking.getResumePoint()).build();
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

    private void addTrack(LessonTrackingDto lessonTrackingDto, boolean isUpdated) {
        LessonTrackingId trackId = setTrackId(lessonTrackingDto);

        LessonTracking lessonTracking = isUpdated ? lessonTrackingRepository.findByTrackId(trackId).orElseThrow(
                () -> new NoSuchElementException(
                        "The track with trackId:[" + trackId.toString() + "] is not exist."))
                : new LessonTracking();
        LessonTracking track = lessonTrackingRepository
                .findTrackedByEnrollmentIdAndCourseId(lessonTrackingDto.getEnrollmentId(),
                        lessonTrackingDto.getCourseId());
        lessonTrackingRepository.save(track);

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
