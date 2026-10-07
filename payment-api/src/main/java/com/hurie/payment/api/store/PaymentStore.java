package com.hurie.payment.api.store;

import com.hurie.payment.api.model.Payment;
import com.hurie.payment.api.model.PaymentCallback;
import com.hurie.payment.api.model.PaymentStatus;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class PaymentStore {

    private final List<Payment> payments = List.of(
            new Payment(
                    "RC-123",
                    BigDecimal.valueOf(100000),
                    PaymentStatus.PAID,
                    "QRIS"),

            new Payment(
                    "RC-124",
                    BigDecimal.valueOf(250000),
                    PaymentStatus.FAILED,
                    "VA_BCA"),

            new Payment(
                    "RC-125",
                    BigDecimal.valueOf(50000),
                    PaymentStatus.PENDING,
                    "EWALLET")
    );

    private final List<PaymentCallback> callbacks = List.of(

            new PaymentCallback(
                    "RC-123",
                    PaymentStatus.REQUEST,
                    LocalDateTime.now().minusMinutes(5)),

            new PaymentCallback(
                    "RC-123",
                    PaymentStatus.PAID,
                    LocalDateTime.now().minusMinutes(3)),

            new PaymentCallback(
                    "RC-124",
                    PaymentStatus.FAILED,
                    LocalDateTime.now().minusMinutes(2))
    );

    public List<Payment> allPayments() {
        return payments;
    }

    public Optional<Payment> findPayment(String orderId) {
        return payments
                .stream()
                .filter(p -> p.orderId().equals(orderId))
                .findFirst();
    }

    public List<PaymentCallback> allCallbacks() {
        return callbacks;
    }

    public long countFailed() {
        return payments
                .stream()
                .filter(p -> PaymentStatus.FAILED.equals(p.status()))
                .count();
    }
}
