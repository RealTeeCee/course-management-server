package com.aptech.coursemanagementserver.services;

import java.util.List;

import com.aptech.coursemanagementserver.dtos.OrderDto;

public interface OrderService {
    public OrderDto findById(long id);

    public List<OrderDto> findByUserId(long id);

    public OrderDto findByTransactionId(String transactionId);

    public List<OrderDto> findAll();
}
