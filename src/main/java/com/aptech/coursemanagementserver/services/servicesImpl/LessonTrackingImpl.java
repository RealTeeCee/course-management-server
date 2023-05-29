package com.aptech.coursemanagementserver.services.servicesImpl;

import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import com.aptech.coursemanagementserver.dtos.LessonTrackingDto;
import com.aptech.coursemanagementserver.exceptions.BadRequestException;
import com.aptech.coursemanagementserver.models.LessonTracking;
import com.aptech.coursemanagementserver.models.LessonTrackingId;
import com.aptech.coursemanagementserver.repositories.LessonTrackingRepository;
import com.aptech.coursemanagementserver.services.LessonTrackingService;
import static com.aptech.coursemanagementserver.constants.GlobalStorage.*;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LessonTrackingImpl implements LessonTrackingService {
    private final LessonTrackingRepository lessonTrackingRepository;

    @Override
    public boolean trackLesson(LessonTrackingDto lessonTrackingDto) {
        try {
            boolean isUpdated = lessonTrackingDto.getOld_lessonId() > 0;

            LessonTracking lessonTracking = new LessonTracking();

            if (isUpdated) {
                LessonTrackingId trackId = new LessonTrackingId();

                trackId.setEnrollment_id(lessonTrackingDto.getEnrollmentId())
                        .setCourse_id(lessonTrackingDto.getCourseId())
                        .setLession_id(lessonTrackingDto.getOld_lessonId())
                        .setSection_id(lessonTrackingDto.getOld_sectionId());
                lessonTracking = lessonTrackingRepository.findById(trackId).orElseThrow(
                        () -> new NoSuchElementException(
                                "The track with trackId is not exist."));
                lessonTrackingRepository.delete(lessonTracking);
                addLessonTracking(lessonTrackingDto);
            } else {
                addLessonTracking(lessonTrackingDto);
            }

            return true;
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException(BAD_REQUEST_EXCEPTION);
        }
    }

    private void addLessonTracking(LessonTrackingDto lessonTrackingDto) {
        LessonTracking newLessonTracking = new LessonTracking();
        LessonTrackingId newTrackId = new LessonTrackingId();

        newTrackId.setEnrollment_id(lessonTrackingDto.getEnrollmentId())
                .setCourse_id(lessonTrackingDto.getCourseId())
                .setLession_id(lessonTrackingDto.getLessonId()).setSection_id(lessonTrackingDto.getSectionId());
        newLessonTracking.setTrackingId(newTrackId);
        lessonTrackingRepository.save(newLessonTracking);
    }

}
