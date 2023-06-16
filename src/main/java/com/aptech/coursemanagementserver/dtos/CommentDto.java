package com.aptech.coursemanagementserver.dtos;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDto {
    private long id;

    private long userId;
    private String content;

    private long postId;

    @Builder.Default
    private Instant created_at = Instant.now();
}
