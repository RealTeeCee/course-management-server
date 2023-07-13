package com.aptech.coursemanagementserver.controllers;

import static com.aptech.coursemanagementserver.constants.GlobalStorage.GLOBAL_EXCEPTION;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aptech.coursemanagementserver.dtos.OrderDto;
import com.aptech.coursemanagementserver.dtos.OrderHistoryRequestDto;
import com.aptech.coursemanagementserver.exceptions.BadRequestException;
import com.aptech.coursemanagementserver.services.OrderService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
@Tag(name = "Orders Endpoints")
public class OrderController {
    private final OrderService orderService;

    @PostMapping(path = "/history")
    @Operation(summary = "[USER] - Get Order History")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<Page<OrderDto>> getOrderHistory(@RequestBody OrderHistoryRequestDto dto) {
        try {
            return ResponseEntity.ok(orderService.findByUserId(dto));
        } catch (Exception e) {
            throw new BadRequestException(GLOBAL_EXCEPTION);
        }
    }

    @PostMapping(path = "/history/refund")
    @Operation(summary = "[USER] - Get Order History Refund")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<Page<OrderDto>> getOrderHistoryRefund(@RequestBody OrderHistoryRequestDto dto) {
        try {
            return ResponseEntity.ok(orderService.findInCompletedByUserId(dto));
        } catch (Exception e) {
            throw new BadRequestException(GLOBAL_EXCEPTION);
        }
    }
}
