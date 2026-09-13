package com.hurie.payment.api.service;

import com.hurie.payment.api.model.Payment;
import com.hurie.payment.api.model.PaymentCallback;
import com.hurie.payment.api.store.PaymentStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PaymentService {

    private final PaymentStore store;

    public Payment getPayment(String orderId) {

        return store.payments
                .stream()
                .filter(p -> p.orderId().equals(orderId))
                .findFirst()
                .orElseThrow();
    }

    public List<PaymentCallback> getCallbacks(
            String orderId) {

        return store.callbacks
                .stream()
                .filter(c ->
                        c.orderId().equals(orderId))
                .toList();
    }

    public long countFailedTransaction() {

        return store.payments
                .stream()
                .filter(p ->
                        "FAILED".equals(p.status()))
                .count();
    }

    public List<Payment> getPaymentInfo() {

        return store.payments.stream().toList();
    }
}
