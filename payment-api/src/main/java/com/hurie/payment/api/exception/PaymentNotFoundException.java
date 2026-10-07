package com.hurie.payment.api.exception;

public class PaymentNotFoundException extends RuntimeException {

    private final String orderId;

    public PaymentNotFoundException(String orderId) {
        super("Payment not found for order id: " + orderId);
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }
}
