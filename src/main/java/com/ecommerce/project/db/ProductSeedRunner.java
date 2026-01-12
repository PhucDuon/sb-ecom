package com.ecommerce.project.db;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("seed")
@RequiredArgsConstructor
public class ProductSeedRunner implements ApplicationRunner {

    private final ProductImportService importService;

    @Override
    public void run(ApplicationArguments args) {
        importService.importProducts();
    }
}
