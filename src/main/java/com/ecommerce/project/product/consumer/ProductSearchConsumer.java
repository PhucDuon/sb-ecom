package com.ecommerce.project.product.consumer;

import com.ecommerce.project.product.event.ProductEvent;
import com.ecommerce.project.product.model.ProductDocument;
import com.ecommerce.project.product.repository.ProductSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductSearchConsumer {

    private final ProductSearchRepository searchRepository;

    @RabbitListener(queues = "product.search.queue")
    public void handle(ProductEvent event) {

        if (event.type() == ProductEvent.EventType.DELETED) {
            searchRepository.deleteById(event.productId());
            return;
        }

        ProductDocument doc = ProductDocument.builder()
                .id(event.productId())
                .name(event.name())
                .description(event.description())
                .price(event.price())
                .stock(event.stock())
                .active(event.active())
                .build();

        searchRepository.save(doc);
    }
}
