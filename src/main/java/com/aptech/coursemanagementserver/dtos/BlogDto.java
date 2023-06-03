package com.aptech.coursemanagementserver.dtos;

import java.time.LocalDateTime;

import com.aptech.coursemanagementserver.enums.BlogStatus;

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
public class BlogDto {
    private long id;
    private String name;
    private String slug;
    private BlogStatus status;
    private String description;
    private int view_count;
    private long user_id;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
}
