package com.hurie.payment.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class PaymentExceptionHandler {

    @ExceptionHandler(PaymentNotFoundException.class)
    public ResponseEntity<Map<String, String>> handlePaymentNotFound(PaymentNotFoundException exception) {

        Map<String, String> body = Map.of(
                "error", "Payment not found",
                "orderId", exception.getOrderId());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }
}
