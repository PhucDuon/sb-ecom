package com.ecommerce.project.reporting.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OverviewReportDto {

    private long totalOrders;
    private BigDecimal totalRevenue;
    private long paidOrders;
    private long failedPayments;
}

