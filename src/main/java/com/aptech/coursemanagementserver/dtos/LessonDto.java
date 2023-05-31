package com.aptech.coursemanagementserver.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class LessonDto {
    private long id;
    private String name;
    private String description;
    private int duration;
    private long sectionId;
    @Builder.Default
    private int status = 1;
}
