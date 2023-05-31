package com.aptech.coursemanagementserver.services.paymentServices;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.paypal.api.payments.Address;
import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Item;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.PayerInfo;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.Presentation;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.ShippingAddress;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaypalService {
    private final APIContext apiContext;

    public Payment createPayment(
            Double total,
            String currency,
            String method,
            String intent,
            String description,
            long userId,
            long courseId,
            String cancelUrl,
            String successUrl) throws PayPalRESTException {
        Amount amount = new Amount();
        amount.setCurrency(currency);
        total = new BigDecimal(total).setScale(2, RoundingMode.HALF_UP).doubleValue();
        amount.setTotal(String.format("%.2f", total));

        Transaction transaction = new Transaction();
        transaction.setDescription(description);
        transaction.setAmount(amount);

        // Set shipping address
        ShippingAddress shippingAddress = new ShippingAddress();
        shippingAddress.setLine1("123 Main St");
        shippingAddress.setLine2("Apt 4B");
        shippingAddress.setCity("San Francisco");
        shippingAddress.setState("CA");
        shippingAddress.setPostalCode("94107");
        shippingAddress.setCountryCode("US");

        Address billingAddress = new Address();
        billingAddress.setLine1("123 Main St");
        billingAddress.setLine2("Apt 4B");
        billingAddress.setCity("San Francisco");
        billingAddress.setState("CA");
        billingAddress.setPostalCode("94107");
        billingAddress.setCountryCode("US");

        // Set custom field
        Item item = new Item();
        String sku = "courseId:" + courseId + "|userId:" + userId;
        item.setSku(sku);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        // Set payer info
        Payer payer = new Payer();
        payer.setPaymentMethod(method.toString());

        // Display custom user
        PayerInfo payerInfo = new PayerInfo();
        payerInfo.setFirstName("Your First Name");
        payerInfo.setLastName("Your Last Name");
        payerInfo.setEmail("youremail@example.com");
        payerInfo.setShippingAddress(shippingAddress);
        payerInfo.setBillingAddress(billingAddress);

        payer.setPayerInfo(payerInfo);

        Payment payment = new Payment();

        // Create presentation object
        Presentation presentation = new Presentation();
        presentation.setBrandName("My Custom Brand Name");
        presentation.setLogoImage("http://localhost:8080/my-logo");

        // Set the payment experience ID
        payment.setExperienceProfileId("experience_profile_id");

        payment.setIntent(intent);
        payment.setPayer(payer);
        payment.setTransactions(transactions);
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(cancelUrl);
        redirectUrls.setReturnUrl(successUrl);
        payment.setRedirectUrls(redirectUrls);

        return payment.create(apiContext);
    }

    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException {
        Payment payment = new Payment();
        payment.setId(paymentId);
        PaymentExecution paymentExecute = new PaymentExecution();
        paymentExecute.setPayerId(payerId);
        return payment.execute(apiContext, paymentExecute);
    }

}
