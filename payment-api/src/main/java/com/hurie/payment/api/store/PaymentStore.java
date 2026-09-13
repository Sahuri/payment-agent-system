package com.hurie.payment.api.store;

import com.hurie.payment.api.model.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class PaymentStore {

    public final List<Payment> payments = List.of(
            new Payment(
                    "RC-123",
                    BigDecimal.valueOf(100000),
                    "PAID",
                    "QRIS"),

            new Payment(
                    "RC-124",
                    BigDecimal.valueOf(250000),
                    "FAILED",
                    "VA_BCA"),

            new Payment(
                    "RC-125",
                    BigDecimal.valueOf(50000),
                    "PENDING",
                    "EWALLET")
    );

    public final List<PaymentCallback> callbacks = List.of(

            new PaymentCallback(
                    "RC-123",
                    "REQUEST",
                    LocalDateTime.now().minusMinutes(5)),

            new PaymentCallback(
                    "RC-123",
                    "PAID",
                    LocalDateTime.now().minusMinutes(3)),

            new PaymentCallback(
                    "RC-124",
                    "FAILED",
                    LocalDateTime.now().minusMinutes(2))
    );
}