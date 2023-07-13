package com.aptech.coursemanagementserver.services;

import java.util.List;

import org.springframework.data.domain.Page;

import com.aptech.coursemanagementserver.dtos.OrderDto;
import com.aptech.coursemanagementserver.dtos.OrderHistoryRequestDto;

public interface OrderService {
    public OrderDto findById(long id);

    public Page<OrderDto> findByUserId(OrderHistoryRequestDto dto);

    public Page<OrderDto> findInCompletedByUserId(OrderHistoryRequestDto dto);

    public OrderDto findByTransactionId(String transactionId);

    public List<OrderDto> findAll();
}
