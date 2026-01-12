package com.ecommerce.project.db;

import com.ecommerce.project.product.model.Product;
import com.ecommerce.project.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductImportService {

    private final ProductRepository productRepository;

    private static final int BATCH_SIZE = 1000;

    @Transactional
    public void importProducts() {

        List<Product> batch = new ArrayList<>();

        for (int i = 1; i <= 50_000; i++) {

            Product product = Product.builder()
                    .name("Product " + i)
                    .description("Description for product " + i)
                    .price(BigDecimal.valueOf(10.0 + (i % 100)))
                    .stock(100)
                    .active(true)
                    .build();

            batch.add(product);

            if (i % BATCH_SIZE == 0) {
                productRepository.saveAll(batch);
                batch.clear();
            }
        }

        // insert nốt phần còn lại
        if (!batch.isEmpty()) {
            productRepository.saveAll(batch);
        }
    }
}

