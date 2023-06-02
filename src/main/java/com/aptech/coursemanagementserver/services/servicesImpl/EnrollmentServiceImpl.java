package com.aptech.coursemanagementserver.services.servicesImpl;

import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import com.aptech.coursemanagementserver.dtos.EnrollmentDto;
import com.aptech.coursemanagementserver.dtos.baseDto.BaseDto;
import com.aptech.coursemanagementserver.enums.AntType;
import com.aptech.coursemanagementserver.exceptions.BadRequestException;
import com.aptech.coursemanagementserver.models.Course;
import com.aptech.coursemanagementserver.models.Enrollment;
import com.aptech.coursemanagementserver.repositories.CourseRepository;
import com.aptech.coursemanagementserver.repositories.EnrollmentRepository;
import com.aptech.coursemanagementserver.repositories.UserRepository;
import com.aptech.coursemanagementserver.services.EnrollmentService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {
    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    @Override
    public BaseDto enroll(EnrollmentDto enrollmentDto) {
        try {
            // Enroll Free courses
            Course course = courseRepository.findById(enrollmentDto.getCourse_id())
                    .orElseThrow(() -> new NoSuchElementException(
                            "The course with courseId: [" + enrollmentDto.getCourse_id() + "] is not exist."));

            boolean isFreeCourse = course.getPrice() == 0;

            if (isFreeCourse) {
                Enrollment enrollment = new Enrollment();
                enrollment.setComment(enrollmentDto.getComment()).setCourse(course)
                        .setIsNotify(enrollmentDto.isNotify()).setProgress(0)
                        .setRating(enrollmentDto.getRating())
                        .setUser(userRepository.findById(enrollmentDto.getUser_id())
                                .orElseThrow(() -> new NoSuchElementException(
                                        "The user with userId: [" + enrollmentDto.getUser_id() + "] is not exist.")));

                enrollmentRepository.save(enrollment);
            }

            Enrollment enrollment = new Enrollment();
            enrollment.setComment(enrollment.getComment()).setCourse(course)
                    .setIsNotify(enrollmentDto.isNotify()).setProgress(enrollmentDto.getProgress())
                    .setRating(enrollmentDto.getRating())
                    .setUser(userRepository.findById(enrollmentDto.getUser_id())
                            .orElseThrow(() -> new NoSuchElementException(
                                    "The user with userId: [" + enrollmentDto.getUser_id() + "] is not exist.")));

            enrollmentRepository.save(enrollment);

            // Tạm thời save trước để test api tracking

            return BaseDto.builder().type(AntType.success).message("Enroll successfully.").build();
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }

    }

    @Override
    public Long getEnrollId(EnrollmentDto enrollmentDto) {

        Enrollment enroll = enrollmentRepository.getEnrollByCourseIdAndUserId(enrollmentDto.getCourse_id(),
                enrollmentDto.getUser_id());
        if (enroll != null) {
            return enroll.getId();
        }
        return 0L;
    }

}
