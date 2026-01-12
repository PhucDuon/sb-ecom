package com.ecommerce.project.product.repository;

import com.ecommerce.project.product.model.ProductDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ProductSearchRepository
        extends ElasticsearchRepository<ProductDocument, String> {
}
