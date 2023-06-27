package com.aptech.coursemanagementserver.controllers;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aptech.coursemanagementserver.dtos.NotificationDto;
import com.aptech.coursemanagementserver.exceptions.BadRequestException;
import com.aptech.coursemanagementserver.exceptions.ResourceNotFoundException;
import com.aptech.coursemanagementserver.services.NotificationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import static com.aptech.coursemanagementserver.constants.GlobalStorage.GLOBAL_EXCEPTION;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notification")
@Tag(name = "Notification Endpoints")
public class NotificationController {
    private final NotificationService notifService;

    @GetMapping("/{userToId}")
    @Operation(summary = "[ANY ROLE] - Get Notification By UserToId")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MANAGER', 'EMPLOYEE')")
    public ResponseEntity<List<NotificationDto>> getNotificationsByUserToId(@PathVariable int userToId) {
        try {
            return ResponseEntity.ok(notifService.findAllByUserToId(userToId));
        } catch (NoSuchElementException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException(GLOBAL_EXCEPTION);
        }
    }

    @PatchMapping("/read/{notifId}")
    @Operation(summary = "[ANY ROLE] - Update Status To Read By NotifId")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MANAGER', 'EMPLOYEE')")
    public ResponseEntity<NotificationDto> changeNotifStatusToRead(@PathVariable long notifId) {
        try {
            return ResponseEntity.ok(notifService.updateStatusToRead(notifId));
        } catch (NoSuchElementException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException(GLOBAL_EXCEPTION);
        }
    }

    @PatchMapping("/read-all/{userToId}")
    @Operation(summary = "[ANY ROLE] - Update All Status To Read By UserToId")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MANAGER', 'EMPLOYEE')")
    public ResponseEntity<List<NotificationDto>> changeAllNotifStatusToRead(@PathVariable long userToId) {
        try {
            return ResponseEntity.ok(notifService.updateAllStatusToRead(userToId));
        } catch (NoSuchElementException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException(GLOBAL_EXCEPTION);
        }
    }
}
