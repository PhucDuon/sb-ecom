package com.ecommerce.project.order.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PaymentService {

    public boolean pay(Long orderId, BigDecimal amount) {

        return Math.random() > 0.2;
    }
}
