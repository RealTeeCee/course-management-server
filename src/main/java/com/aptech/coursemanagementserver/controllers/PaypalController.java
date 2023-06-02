package com.aptech.coursemanagementserver.controllers;

import static com.aptech.coursemanagementserver.constants.GlobalStorage.DEV_DOMAIN_CLIENT;
import static com.aptech.coursemanagementserver.constants.GlobalStorage.GLOBAL_EXCEPTION;
import static com.aptech.coursemanagementserver.constants.GlobalStorage.PAYPAL_CANCEL_API;
import static com.aptech.coursemanagementserver.constants.GlobalStorage.PAYPAL_CANCEL_URL;
import static com.aptech.coursemanagementserver.constants.GlobalStorage.PAYPAL_SUCCESS_API;
import static com.aptech.coursemanagementserver.constants.GlobalStorage.PAYPAL_SUCCESS_URL;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.aptech.coursemanagementserver.dtos.payment.PaypalRequestDto;
import com.aptech.coursemanagementserver.exceptions.BadRequestException;
import com.aptech.coursemanagementserver.services.paymentServices.PaypalService;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/paypal")
@Tag(name = "Paypal Payment Endpoints")

public class PaypalController {
    private final PaypalService service;

    @GetMapping(path = "/")
    public RedirectView home() {
        return new RedirectView(
                "https://www.sandbox.paypal.com/cgi-bin/webscr?cmd=_express-checkout&token=EC-6G2428565Y737991B");
    }

    @PostMapping(path = "/pay")
    public RedirectView payment(@RequestBody PaypalRequestDto dto) {
        try {
            Payment payment = service.createPayment(dto, PAYPAL_CANCEL_API,
                    PAYPAL_SUCCESS_API);
            for (Links link : payment.getLinks()) {
                if (link.getRel().equals("approval_url")) {
                    return new RedirectView(link.getHref());
                }
            }

        } catch (PayPalRESTException e) {

            e.printStackTrace();
        }
        return new RedirectView(DEV_DOMAIN_CLIENT);
    }

    @GetMapping(path = PAYPAL_CANCEL_URL)
    public String cancelPay() {
        return "cancel";
    }

    @GetMapping(path = PAYPAL_SUCCESS_URL)
    public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
        try {
            Payment payment = service.executePayment(paymentId, payerId);
            System.out.println(payment.toJSON());
            if (payment.getState().equals("approved")) {

                return "success";
            }
        } catch (PayPalRESTException e) {
            throw new BadRequestException(GLOBAL_EXCEPTION);
        }
        return "redirect:/";
    }
}
