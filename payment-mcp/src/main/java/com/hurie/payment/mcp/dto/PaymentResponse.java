package com.hurie.payment.mcp.dto;

import java.math.BigDecimal;

public record PaymentResponse(
        String orderId,
        BigDecimal amount,
        String status,
        String channel
) {
}
