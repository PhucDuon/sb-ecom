package com.ecommerce.project.reporting.service;

import com.ecommerce.project.product.model.Product;
import com.ecommerce.project.reporting.dto.OverviewReportDto;
import com.ecommerce.project.reporting.dto.RevenueByDateDto;
import com.ecommerce.project.reporting.dto.UserCountReport;
import com.ecommerce.project.reporting.repository.OrderReportRepository;
import com.ecommerce.project.reporting.repository.PaymentReportRepository;
import com.ecommerce.project.reporting.repository.ProductReportRepository;
import com.ecommerce.project.reporting.repository.UserCountReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportingService {

    private final OrderReportRepository orderRepo;
    private final PaymentReportRepository paymentRepo;
    private final ProductReportRepository productRepo;
    private final UserCountReportRepository userRepository;

    public OverviewReportDto overview() {

        long totalOrders = orderRepo.countOrders();
        BigDecimal revenue = orderRepo.totalRevenue();
        long paidOrders = orderRepo.countPaidOrders();
        long failedPayments = paymentRepo.countFailed();

        return new OverviewReportDto(
                totalOrders,
                revenue,
                paidOrders,
                failedPayments
        );
    }

    public List<RevenueByDateDto> revenueByMonth(LocalDate from, LocalDate to) {
        return orderRepo.revenueByMonth(
                from.atStartOfDay(),
                to.plusDays(1).atStartOfDay()
        );
    }


    public List<Product> getLowStockProducts(int threshold) {
        return productRepo.findLowStock(threshold);
    }

    public List<UserCountReport> newUsers(String range) {

        List<Object[]> raw;
        int labelIndex;
        int countIndex;

        switch (range) {

            case "DAY" -> {
                raw = userRepository.countNewUsersPerDay();
                labelIndex = 0;
                countIndex = 1;
            }

            case "WEEK" -> {
                raw = userRepository.countNewUsersPerWeek();
                labelIndex = -1; // custom
                countIndex = 2;
            }

            case "MONTH" -> {
                raw = userRepository.countNewUsersPerMonth();
                labelIndex = -2; // custom
                countIndex = 2;
            }

            case "YEAR" -> {
                raw = userRepository.countNewUsersPerYear();
                labelIndex = 0;
                countIndex = 1;
            }

            default -> throw new IllegalArgumentException("Invalid range");
        }

        return raw.stream()
                .map(r -> new UserCountReport(
                        buildLabel(range, r, labelIndex),
                        ((Number) r[countIndex]).longValue()
                ))
                .toList();
    }
    private String buildLabel(String range, Object[] r, int labelIndex) {

        return switch (range) {

            case "DAY" ->
                    r[0].toString();

            case "WEEK" ->
                    r[0] + "-W" + r[1];

            case "MONTH" ->
                    r[0] + "-" + String.format("%02d", r[1]);

            case "YEAR" ->
                    r[0].toString();

            default -> "";
        };
    }


}
