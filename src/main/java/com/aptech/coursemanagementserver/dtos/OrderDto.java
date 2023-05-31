package com.aptech.coursemanagementserver.dtos;

import com.aptech.coursemanagementserver.enums.OrderStatus;
import com.aptech.coursemanagementserver.enums.payment.PaymentType;

public class OrderDto {
    private long id;
    private String userName;
    private String courseName;
    private String description;
    private int duration;
    private double price;
    private double net_price;
    private PaymentType payment = PaymentType.PAYPAL;
    private OrderStatus status;
}
