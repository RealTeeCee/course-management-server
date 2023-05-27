package com.aptech.coursemanagementserver.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LessonTrackingDto {
    // Select Lesson ->
    // notexist -> create
    // exist -> update
    // Return tracking lesson

    private long old_sectionId;
    private long old_lessonId;
    private long courseId;
    private long sectionId;
    private long lessonId;
    private long enrollmentId;

    // Update LessonTracking set sectionId ='new SectionID', lessonId ='new
    // SectionID' where
    // courseId ='courseId' and enrollmentId= 'enrollmentId' and sectionId ='old
    // section' and lessonId = 'old lessonId';

    // old -> previous State

}
