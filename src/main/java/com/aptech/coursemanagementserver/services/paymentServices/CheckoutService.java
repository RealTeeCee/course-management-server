package com.aptech.coursemanagementserver.services.paymentServices;

import static com.aptech.coursemanagementserver.constants.GlobalStorage.MOMO_CHECKOUT_API;
import static com.aptech.coursemanagementserver.constants.GlobalStorage.PAYPAL_CHECKOUT_API;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.aptech.coursemanagementserver.dtos.payment.CheckoutDto;
import com.aptech.coursemanagementserver.dtos.payment.MomoRequestDto;
import com.aptech.coursemanagementserver.dtos.payment.PaypalRequestDto;
import com.aptech.coursemanagementserver.dtos.payment.PaypalResponseDto;
import com.aptech.coursemanagementserver.enums.payment.PaymentType;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CheckoutService {
    private final RestTemplate restTemplate;

    public ResponseEntity<?> checkoutPayment(CheckoutDto checkoutDto) {
        if (checkoutDto.getPaymentType() == PaymentType.PAYPAL) {
            PaypalRequestDto paypalRequestDto = new PaypalRequestDto();
            paypalRequestDto.setCourseId(checkoutDto.getCourseId());
            paypalRequestDto.setUserId(checkoutDto.getUserId());
            paypalRequestDto.setPrice(checkoutDto.getAmount());
            PaypalResponseDto response = restTemplate.postForObject(PAYPAL_CHECKOUT_API, paypalRequestDto,
                    PaypalResponseDto.class);
            return ResponseEntity.ok(response);
        }
        MomoRequestDto MomoRequestDto = new MomoRequestDto();
        MomoRequestDto.setCourseId(checkoutDto.getCourseId());
        MomoRequestDto.setUserId(checkoutDto.getUserId());
        MomoRequestDto.setAmount(checkoutDto.getAmount());
        MomoRequestDto response = restTemplate.postForObject(MOMO_CHECKOUT_API, MomoRequestDto, MomoRequestDto.class);
        return ResponseEntity.ok(response);
    }
}
