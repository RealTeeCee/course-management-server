package com.aptech.coursemanagementserver.controllers;

import static com.aptech.coursemanagementserver.constants.GlobalStorage.FETCHING_FAILED;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aptech.coursemanagementserver.dtos.SearchDto;
import com.aptech.coursemanagementserver.dtos.SummaryDashboardDto;
import com.aptech.coursemanagementserver.exceptions.BadRequestException;
import com.aptech.coursemanagementserver.services.HomeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/home")
@Tag(name = "Home Endpoints")
public class HomeController {
    private final HomeService homeService;

    @PostMapping(path = "/search")
    @Operation(summary = "[ANORNYMOUS] - Search All Blogs, Course, Author")
    public ResponseEntity<List<SearchDto>> searchAll(String name) {
        try {
            return ResponseEntity.ok(homeService.searchAll(name));
        } catch (Exception e) {
            throw new BadRequestException(FETCHING_FAILED);
        }
    }

    @GetMapping(path = "/admin/dashboard")
    @Operation(summary = "[ADMIN, MANAGER, EMPLOYEE] - GET Dashboard")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'EMPLOYEE')")
    public ResponseEntity<SummaryDashboardDto> getSummaryDashboard() {
        try {
            return ResponseEntity.ok(homeService.getSummaryDashboard());
        } catch (Exception e) {
            throw new BadRequestException(FETCHING_FAILED);
        }
    }

}
