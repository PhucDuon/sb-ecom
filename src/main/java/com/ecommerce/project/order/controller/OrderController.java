package com.ecommerce.project.order.controller;

import com.ecommerce.project.order.dto.OrderDto;
import com.ecommerce.project.order.dto.OrderRequestDto;
import com.ecommerce.project.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
@RestController
@RequestMapping("/api/orders")
@PreAuthorize("hasRole('USER')")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public OrderDto placeOrder(
           @Valid @RequestBody OrderRequestDto request,
            Principal principal
    ) {
        return orderService.placeOrder(
                request.getProductId(),
                request.getQuantity(),
                principal.getName()
        );
    }


    @GetMapping
    public List<OrderDto> myOrders(Authentication auth) {
        return orderService.myOrders(auth.getName());
    }
}

