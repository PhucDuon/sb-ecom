package com.ecommerce.project.order.consumer;

import com.ecommerce.project.order.model.Order;
import com.ecommerce.project.order.model.OrderStatus;
import com.ecommerce.project.order.repository.OrderRepository;
import com.ecommerce.project.product.model.Product;
import com.ecommerce.project.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class OrderTimeoutConsumer {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final RedissonClient redissonClient;

    @RabbitListener(queues = "order.dlq")
    @Transactional
    public void handleTimeout(Long orderId) {

        Order order = orderRepository.findById(orderId).orElse(null);

        if (order == null) return;

        // idempotent
        if (order.getStatus() != OrderStatus.PENDING_PAYMENT) {
            return;
        }

        rollbackStock(order);
        order.setStatus(OrderStatus.CANCELLED);
    }

    private void rollbackStock(Order order) {
        RLock lock = redissonClient.getLock("lock:product:" + order.getProductId());
        lock.lock();
        try {
            Product product = productRepository.findById(order.getProductId())
                    .orElseThrow();

            product.setStock(product.getStock() + order.getQuantity());
            productRepository.save(product);
        } finally {
            lock.unlock();
        }
    }
}
