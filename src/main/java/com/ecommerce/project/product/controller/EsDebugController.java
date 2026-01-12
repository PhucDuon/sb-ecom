package com.ecommerce.project.product.controller;

import com.ecommerce.project.product.model.ProductDocument;
import com.ecommerce.project.product.repository.ProductSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/debug")
public class EsDebugController {

    private final ProductSearchRepository searchRepository;

    @PostMapping("/es")
    public String testEs() {

        ProductDocument doc = ProductDocument.builder()
                .id("debug-1")
                .name("MacBook Debug")
                .description("Test elasticsearch save")
                .price(BigDecimal.valueOf(2500.0))
                .stock(10)
                .active(true)
                .build();

        searchRepository.save(doc);

        return "SAVED";
    }

    @GetMapping("/es/count")
    public long count() {
        return searchRepository.count();
    }
}

