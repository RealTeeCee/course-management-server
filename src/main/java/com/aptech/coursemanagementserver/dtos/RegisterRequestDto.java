package com.aptech.coursemanagementserver.dtos;

import com.aptech.coursemanagementserver.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestDto {
    private String first_name;
    private String last_name;
    private String email;
    private String password;
    @Builder.Default
    private Role role = Role.USER;
}
