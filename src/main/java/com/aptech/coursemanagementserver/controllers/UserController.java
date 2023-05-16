package com.aptech.coursemanagementserver.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aptech.coursemanagementserver.dtos.UserProfileDto;
import com.aptech.coursemanagementserver.enums.AntType;
import com.aptech.coursemanagementserver.models.User;
import com.aptech.coursemanagementserver.services.authServices.CurrentUser;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/auth")
@Tag(name = "Current User Endpoints")
public class UserController {

    @GetMapping("/user/me")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MANAGER', 'EMPLOYEE')")
    public ResponseEntity<UserProfileDto> getCurrentUser(@CurrentUser User user) {

        var userProfileDto = UserProfileDto.builder().email(user.getEmail())
                .imageUrl(user.getImageUrl())
                .name(user.getName())
                .type(AntType.success)
                .role(user.getRole())
                .message("Get current logged in user success.")
                .build();
        // Authentication authentication) {
        // User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(userProfileDto);
        // return userRepository.findById(user.getId())
        // .orElseThrow(() -> new UserNotFoundException("Username", "Id",
        // user.getId()));
    }
}
