package com.aptech.coursemanagementserver.dtos.payment;

import com.aptech.coursemanagementserver.enums.payment.PaypalIntent;
import com.aptech.coursemanagementserver.enums.payment.PaypalMethod;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class PaypalRequestDto {
    private double price;
    private String currency;
    private PaypalMethod method = PaypalMethod.paypal;
    private PaypalIntent intent = PaypalIntent.sale;
    private String description;
}
