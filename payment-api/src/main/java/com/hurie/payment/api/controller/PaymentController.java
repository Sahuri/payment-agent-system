package com.hurie.payment.api.controller;


import com.hurie.payment.api.model.Payment;
import com.hurie.payment.api.model.PaymentCallback;
import com.hurie.payment.api.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/{orderId}")
    public Payment getPayment(@PathVariable String orderId) {
        return paymentService.getPayment(orderId);
    }

    @GetMapping("/{orderId}/callbacks")
    public List<PaymentCallback> getCallbacks(@PathVariable String orderId) {
        return paymentService.getCallbacks(orderId);
    }

    @GetMapping("/failed/count")
    public long countFailedTransaction() {
        return paymentService.countFailedTransaction();
    }


    @GetMapping("/payment/info")
    public List<Payment> getPaymentInfo() {
        return paymentService.getPaymentInfo();
    }
}
