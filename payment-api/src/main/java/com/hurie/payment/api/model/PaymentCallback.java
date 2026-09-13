package com.hurie.payment.api.model;

import java.time.LocalDateTime;

public record PaymentCallback(
        String orderId,
        String callbackStatus,
        LocalDateTime callbackTime
) {
}
