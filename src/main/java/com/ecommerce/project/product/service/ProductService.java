package com.ecommerce.project.product.service;

import com.ecommerce.project.product.dto.ProductDto;
import com.ecommerce.project.product.dto.ProductRequestDto;
import com.ecommerce.project.product.event.ProductEvent;
import com.ecommerce.project.product.event.ProductEventPublisher;
import com.ecommerce.project.product.model.Product;
import com.ecommerce.project.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductEventPublisher eventPublisher;

    @Transactional
    public ProductDto create(ProductRequestDto req) {

        Product product = Product.builder()
                .name(req.getName())
                .description(req.getDescription())
                .price(req.getPrice())
                .stock(req.getStock())
                .active(true)
                .build();

        Product saved = productRepository.save(product);

        publishEvent(saved, ProductEvent.EventType.CREATED);

        return toDto(saved);
    }

    @Transactional
    public ProductDto update(String id, ProductRequestDto req) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setName(req.getName());
        product.setDescription(req.getDescription());
        product.setPrice(req.getPrice());
        product.setStock(req.getStock());

        Product saved = productRepository.save(product);

        publishEvent(saved, ProductEvent.EventType.UPDATED);

        return toDto(saved);
    }

    @Transactional
    public void delete(String id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setActive(false);

        Product saved = productRepository.save(product);

        publishEvent(saved, ProductEvent.EventType.DELETED);
    }

    public List<ProductDto> getAllActive() {
        return productRepository.findByActiveTrue()
                .stream()
                .map(this::toDto)
                .toList();
    }



    private void publishEvent(Product product, ProductEvent.EventType type) {
        eventPublisher.publish(
                new ProductEvent(
                        product.getId(),
                        product.getName(),
                        product.getDescription(),
                        product.getPrice(),
                        product.getStock(),
                        product.isActive(),
                        type
                )
        );
    }

    private ProductDto toDto(Product p) {
        return ProductDto.builder()
                .id(p.getId())
                .name(p.getName())
                .description(p.getDescription())
                .price(p.getPrice())
                .stock(p.getStock())
                .build();
    }
}
