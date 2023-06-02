package com.aptech.coursemanagementserver.dtos;

import com.aptech.coursemanagementserver.enums.OrderStatus;
import com.aptech.coursemanagementserver.enums.payment.PaymentType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class OrderDto {
    private long id;
    private String userName;
    private String courseName;
    private String description;
    private int duration;
    private String image;
    private double price;
    private double net_price;
    @Builder.Default
    private PaymentType payment = PaymentType.PAYPAL;
    private OrderStatus status;
}
