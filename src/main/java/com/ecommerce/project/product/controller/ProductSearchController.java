package com.ecommerce.project.product.controller;

import com.ecommerce.project.product.model.ProductDocument;
import com.ecommerce.project.product.service.ProductSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductSearchController {

    private final ProductSearchService searchService;

    @GetMapping("/search")
    public Page<ProductDocument> search(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        Pageable pageable = PageRequest.of(page, size);
        return searchService.search(q, minPrice, maxPrice, pageable);
    }

    @GetMapping("/autocomplete")
    public List<String> autocomplete(
            @RequestParam String q,
            @RequestParam(defaultValue = "10") int limit
    ) {
        return searchService.autocomplete(q, limit);
    }


}
