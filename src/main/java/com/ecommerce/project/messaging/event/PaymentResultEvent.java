package com.ecommerce.project.messaging.event;

import java.math.BigDecimal;

public record PaymentResultEvent(
        Long orderId,
        PaymentStatus status,
        BigDecimal amount
) {
    public enum PaymentStatus {
        SUCCESS,
        FAILED
    }
}
