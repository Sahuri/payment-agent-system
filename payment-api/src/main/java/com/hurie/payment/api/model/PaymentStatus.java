package com.hurie.payment.api.model;

public final class PaymentStatus {

    public static final String PAID = "PAID";
    public static final String FAILED = "FAILED";
    public static final String PENDING = "PENDING";
    public static final String REQUEST = "REQUEST";

    private PaymentStatus() {
    }
}
