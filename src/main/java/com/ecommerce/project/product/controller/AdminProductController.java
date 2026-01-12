package com.ecommerce.project.product.controller;

import com.ecommerce.project.product.dto.ProductDto;
import com.ecommerce.project.product.dto.ProductRequestDto;
import com.ecommerce.project.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/products")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminProductController {

    private final ProductService productService;

    @PostMapping
    public ProductDto create(@RequestBody @Valid ProductRequestDto req) {
        return productService.create(req);
    }

    @PutMapping("/{id}")
    public ProductDto update(
            @PathVariable String id,
            @RequestBody @Valid ProductRequestDto req
    ) {
        return productService.update(id, req);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        productService.delete(id);
    }
}
