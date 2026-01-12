package com.ecommerce.project.reporting.dto;

public class LowStockDto {
    private Long productId;
    private String name;
    private int stock;

    public LowStockDto(Long productId, String name, int stock) {
        this.productId = productId;
        this.name = name;
        this.stock = stock;

    }
}
