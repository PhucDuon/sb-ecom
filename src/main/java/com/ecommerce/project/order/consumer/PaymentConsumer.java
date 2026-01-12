package com.ecommerce.project.order.consumer;

import com.ecommerce.project.messaging.event.PaymentRequest;
import com.ecommerce.project.messaging.event.PaymentResultEvent;
import com.ecommerce.project.order.model.Order;
import com.ecommerce.project.order.model.OrderStatus;
import com.ecommerce.project.order.repository.OrderRepository;
import com.ecommerce.project.order.service.PaymentService;
import com.ecommerce.project.product.model.Product;
import com.ecommerce.project.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class PaymentConsumer {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final PaymentService paymentService;
    private final RedissonClient redissonClient;
    private final RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = "payment.request.queue")
    @Transactional
    public void handlePayment(PaymentRequest request) {

        Order order = orderRepository.findById(request.orderId())
                .orElse(null);

        // idempotent guard
        if (order == null || order.getStatus() != OrderStatus.PENDING_PAYMENT) {
            return;
        }

        boolean paid = paymentService.pay(order.getId(), request.amount());

        if (paid) {
            publishResult(order, PaymentResultEvent.PaymentStatus.SUCCESS);
            return;
        }

        // payment failed → rollback stock
        rollbackStock(order);

        publishResult(order, PaymentResultEvent.PaymentStatus.FAILED);
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

    private void publishResult(Order order, PaymentResultEvent.PaymentStatus status) {
        rabbitTemplate.convertAndSend(
                "payment.exchange",
                "payment.result",
                new PaymentResultEvent(
                        order.getId(),
                        status,
                        order.getTotalPrice()
                )
        );
    }
}
