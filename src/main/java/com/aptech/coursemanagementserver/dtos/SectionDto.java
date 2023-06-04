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
public class SectionDto {
    private long id;
    private String name;
    private long courseId;
    @Builder.Default
    private int status = 1;
    @Builder.Default
    private int ordered = 0;
}
