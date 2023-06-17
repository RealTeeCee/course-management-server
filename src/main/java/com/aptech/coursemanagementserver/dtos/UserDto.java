package com.aptech.coursemanagementserver.dtos;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.aptech.coursemanagementserver.enums.Role;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private long id;

    private String first_name;

    private String last_name;

    private String email;

    private String imageUrl;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    @Builder.Default
    private boolean isVerified = false;

    @CreationTimestamp
    @Builder.Default
    private Instant created_at = Instant.now();
    @UpdateTimestamp
    private Instant updated_at;

}
