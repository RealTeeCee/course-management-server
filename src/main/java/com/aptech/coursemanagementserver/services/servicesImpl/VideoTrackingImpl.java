package com.aptech.coursemanagementserver.services.servicesImpl;

import static com.aptech.coursemanagementserver.constants.GlobalStorage.BAD_REQUEST_EXCEPTION;

import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import com.aptech.coursemanagementserver.dtos.VideoTrackingDto;
import com.aptech.coursemanagementserver.exceptions.BadRequestException;
import com.aptech.coursemanagementserver.models.VideoTracking;
import com.aptech.coursemanagementserver.repositories.EnrollmentRepository;
import com.aptech.coursemanagementserver.repositories.VideoRepository;
import com.aptech.coursemanagementserver.repositories.VideoTrackingRepository;
import com.aptech.coursemanagementserver.services.VideoTrackingService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VideoTrackingImpl implements VideoTrackingService {
    private final VideoTrackingRepository videoTrackingRepository;
    private final VideoRepository videoRepository;
    private final EnrollmentRepository enrollmentRepository;

    @Override
    public boolean trackVideo(VideoTrackingDto videoTrackingDto) {
        try {
            enrollmentRepository.findById(videoTrackingDto.getEnrollment_id())
                    .orElseThrow(() -> new NoSuchElementException(
                            "The enrollment with enrollmentId: [" + videoTrackingDto.getEnrollment_id()
                                    + "] is not exist."));

            videoRepository.findById(videoTrackingDto.getVideo_id())
                    .orElseThrow(() -> new NoSuchElementException(
                            "The video with videoId: [" + videoTrackingDto.getVideo_id() + "] is not exist."));

            boolean isUpdated = videoTrackingRepository.findByVideoIdAndEnrollmentId(
                    videoTrackingDto.getVideo_id(),
                    videoTrackingDto.getEnrollment_id()) != null;

            // Create new
            VideoTracking videoTracking = new VideoTracking();

            // Update
            if (isUpdated) {
                videoTracking = videoTrackingRepository.findByVideoIdAndEnrollmentId(
                        videoTrackingDto.getVideo_id(),
                        videoTrackingDto.getEnrollment_id());
            }

            videoTracking.setEnrollment_id(videoTrackingDto.getEnrollment_id())
                    .setVideo_id(videoTrackingDto.getVideo_id()).setResume_point(videoTrackingDto.getResume_point());

            videoTrackingRepository.save(videoTracking);

            return true;
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException(BAD_REQUEST_EXCEPTION);
        }
    }
}
