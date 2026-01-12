package com.ecommerce.project.order.consumer;

import com.ecommerce.project.messaging.event.PaymentResultEvent;
import com.ecommerce.project.order.model.Order;
import com.ecommerce.project.order.model.OrderStatus;
import com.ecommerce.project.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class PaymentResultConsumer {

    private final OrderRepository orderRepository;

    @RabbitListener(queues = "payment.result.queue")
    @Transactional
    public void handlePaymentResult(PaymentResultEvent event) {

        Order order = orderRepository.findById(event.orderId())
                .orElse(null);

        if (order == null) return;

        if (event.status() == PaymentResultEvent.PaymentStatus.SUCCESS) {
            order.setStatus(OrderStatus.PAID);
        } else {
            order.setStatus(OrderStatus.CANCELLED);
        }
    }
}
