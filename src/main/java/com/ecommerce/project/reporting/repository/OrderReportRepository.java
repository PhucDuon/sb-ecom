package com.ecommerce.project.reporting.repository;

import com.ecommerce.project.reporting.dto.RevenueByDateDto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderReportRepository extends Repository<com.ecommerce.project.order.model.Order, Long> {

    @Query("SELECT COUNT(o) FROM Order o")
    long countOrders();

    @Query("""
        SELECT COALESCE(SUM(o.totalPrice), 0)
        FROM Order o
        WHERE o.status = 'PAID'
    """)
    BigDecimal totalRevenue();

    @Query("""
        SELECT COUNT(o)
        FROM Order o
        WHERE o.status = 'PAID'
    """)
    long countPaidOrders();

    @Query("""
    SELECT new com.ecommerce.project.reporting.dto.RevenueByDateDto(
        FUNCTION('to_char', o.createdAt, 'YYYY-MM'),
        SUM(o.totalPrice)
    )
    FROM Order o
    WHERE o.status = 'PAID'
      AND o.createdAt >= :from
      AND o.createdAt < :to
    GROUP BY FUNCTION('to_char', o.createdAt, 'YYYY-MM')
    ORDER BY FUNCTION('to_char', o.createdAt, 'YYYY-MM')
""")
    List<RevenueByDateDto> revenueByMonth(
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );






}
