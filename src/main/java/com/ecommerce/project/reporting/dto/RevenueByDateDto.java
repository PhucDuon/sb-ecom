package com.ecommerce.project.reporting.dto;

import java.math.BigDecimal;

public class RevenueByDateDto {

    private String month;
    private BigDecimal revenue;


    public RevenueByDateDto(Object month, Number revenue) {
        this.month = month == null ? null : month.toString();
        this.revenue = revenue == null
                ? BigDecimal.ZERO
                : BigDecimal.valueOf(revenue.doubleValue());
    }

    public String getMonth() {
        return month;
    }

    public BigDecimal getRevenue() {
        return revenue;
    }
}
