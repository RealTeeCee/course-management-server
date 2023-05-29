package com.aptech.coursemanagementserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.aptech.coursemanagementserver.models.VideoTracking;

public interface VideoTrackingRepository extends JpaRepository<VideoTracking, Long> {
    @Query(value = """
            SELECT vt.* FROM video_tracking vt
            WHERE vt.video_id = :videoId
            AND vt.enrollment_id = :enrollmentId
                """, nativeQuery = true)
    VideoTracking findByVideoIdAndEnrollmentId(long videoId, long enrollmentId);
}
