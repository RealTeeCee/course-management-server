package com.aptech.coursemanagementserver.dtos;

import java.time.Instant;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

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
public class PostDto {
    private long id;
    private String content;

    private long userId;

    private List<Long> likedUsersId;

    private List<Long> commentsId;

    private String postImageUrl;

    @CreationTimestamp
    @Builder.Default
    private Instant created_at = Instant.now();
}
