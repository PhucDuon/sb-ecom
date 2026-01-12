package com.ecommerce.project.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProductRabbitConfig {

    public static final String PRODUCT_EXCHANGE = "product.exchange";
    public static final String PRODUCT_SEARCH_QUEUE = "product.search.queue";
    public static final String PRODUCT_ROUTING_KEY = "product.event";

    @Bean
    public TopicExchange productExchange() {
        return new TopicExchange(PRODUCT_EXCHANGE);
    }

    @Bean
    public Queue productSearchQueue() {
        return QueueBuilder
                .durable(PRODUCT_SEARCH_QUEUE)
                .build();
    }

    @Bean
    public Binding productSearchBinding() {
        return BindingBuilder
                .bind(productSearchQueue())
                .to(productExchange())
                .with(PRODUCT_ROUTING_KEY);
    }
}
