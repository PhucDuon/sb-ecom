package com.ecommerce.project.order.service;

import com.ecommerce.project.messaging.event.PaymentRequest;
import com.ecommerce.project.order.dto.OrderDto;
import com.ecommerce.project.order.model.Order;
import com.ecommerce.project.order.model.OrderStatus;
import com.ecommerce.project.order.repository.OrderRepository;
import com.ecommerce.project.product.model.Product;
import com.ecommerce.project.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final RedissonClient redissonClient;
    private final RabbitTemplate rabbitTemplate;

 // place order
    @Transactional
    public OrderDto placeOrder(String productId, int quantity, String userEmail) {

        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }

        if (productId == null || productId.isBlank()) {
            throw new IllegalArgumentException("ProductId is required");
        }




        RLock lock = redissonClient.getLock("lock:product:" + productId);

        try {
            if (!lock.tryLock(3, 10, TimeUnit.SECONDS)) {
                throw new RuntimeException("System busy, please try again");
            }

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            if (!product.isActive()) {
                throw new IllegalStateException("Product inactive");
            }

            if (product.getStock() < quantity) {
                throw new RuntimeException("Out of stock");
            }

            // reserve stock
            product.setStock(product.getStock() - quantity);
            productRepository.save(product);

            // create order
            Order order = orderRepository.save(
                    Order.builder()
                            .productId(productId)
                            .quantity(quantity)
                            .totalPrice(
                                    product.getPrice().multiply(BigDecimal.valueOf(quantity))
                            )
                            .status(OrderStatus.PENDING_PAYMENT)
                            .userEmail(userEmail)
                            .build()
            );//order timoeut
            rabbitTemplate.convertAndSend(
                    "order.exchange",
                    "order.delay",
                    order.getId()
            );

            //  async payment
            rabbitTemplate.convertAndSend(
                    "payment.exchange",
                    "payment.request",
                    new PaymentRequest(
                            order.getId(),
                            order.getTotalPrice()
                    )
            );


            return toDto(order);

        } catch (InterruptedException e) {
            throw new RuntimeException("Could not acquire lock", e);

        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    //query

    public List<OrderDto> myOrders(String userEmail) {
        return orderRepository.findByUserEmail(userEmail)
                .stream()
                .map(this::toDto)
                .toList();
    }

    public List<OrderDto> allOrders() {
        return orderRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    public void updateStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(status);
        orderRepository.save(order);
    }

    private OrderDto toDto(Order order) {
        return OrderDto.builder()
                .id(order.getId())
                .productId(order.getProductId())
                .quantity(order.getQuantity())
                .totalPrice(order.getTotalPrice())
                .status(order.getStatus().name())
                .build();
    }
}