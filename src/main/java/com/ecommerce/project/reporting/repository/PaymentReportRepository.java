package com.ecommerce.project.reporting.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

public interface PaymentReportRepository extends Repository<com.ecommerce.project.order.model.Payment, Long> {

    @Query("SELECT COUNT(p) FROM Payment p WHERE p.status = 'SUCCESS'")
    long countSuccess();

    @Query("SELECT COUNT(p) FROM Payment p WHERE p.status = 'FAILED'")
    long countFailed();
}
