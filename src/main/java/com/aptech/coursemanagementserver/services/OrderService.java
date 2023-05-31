package com.aptech.coursemanagementserver.services;

import com.aptech.coursemanagementserver.dtos.OrderDto;

public interface OrderService {
    public boolean save(OrderDto orderDto);
}
