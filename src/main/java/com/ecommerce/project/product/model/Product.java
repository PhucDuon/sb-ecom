package com.ecommerce.project.product.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document(collection = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    private String id;

    private String name;
    private String description;

    private BigDecimal price;
    private int stock;

    private boolean active = true;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

