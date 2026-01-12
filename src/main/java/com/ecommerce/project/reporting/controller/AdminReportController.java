package com.ecommerce.project.reporting.controller;

import com.ecommerce.project.product.model.Product;
import com.ecommerce.project.reporting.dto.OverviewReportDto;
import com.ecommerce.project.reporting.dto.RevenueByDateDto;
import com.ecommerce.project.reporting.dto.UserCountReport;
import com.ecommerce.project.reporting.service.ReportingService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/admin/reports")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminReportController {

    private final ReportingService reportingService;


    @GetMapping("/overview")
    public OverviewReportDto overview() {
        return reportingService.overview();
    }

    @GetMapping("/revenue")
    public List<RevenueByDateDto> revenue(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        return reportingService.revenueByMonth(from, to);
    }

    @GetMapping("/products/low-stock")
    public List<Product> lowStock(
            @RequestParam(defaultValue = "10") int threshold) {
        return reportingService.getLowStockProducts(threshold);
    }

    @GetMapping("/new-users")
    public List<UserCountReport> newUsers(
            @RequestParam String range
    ) {
        return reportingService.newUsers(range);
    }

}
