package com.ecommerce.project.product.event;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publish(ProductEvent event) {
        rabbitTemplate.convertAndSend(
                "product.exchange",
                "product.event",
                event
        );
    }
}
