package com.hurie.payment.api.model;

import java.math.BigDecimal;

public record Payment(
        String orderId,
        BigDecimal amount,
        String status,
        String channel
) {
}