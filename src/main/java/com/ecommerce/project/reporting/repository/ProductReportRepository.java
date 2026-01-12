package com.ecommerce.project.reporting.repository;

import com.ecommerce.project.product.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ProductReportRepository
        extends MongoRepository<Product, String> {

    @Query("{ 'stock': { $lt: ?0 } }")
    List<Product> findLowStock(int threshold);
}
