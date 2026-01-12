package com.ecommerce.project.product.event;

import java.math.BigDecimal;

public record ProductEvent(
        String productId,
        String name,
        String description,
        BigDecimal price,
        int stock,
        boolean active,
        EventType type
) {
    public enum EventType {
        CREATED,
        UPDATED,
        DELETED
    }
}
