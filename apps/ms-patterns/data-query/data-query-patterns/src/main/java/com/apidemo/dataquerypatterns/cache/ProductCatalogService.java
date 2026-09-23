package com.apidemo.dataquerypatterns.cache;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;

@Service
public class ProductCatalogService {
    private final ProductRepository products;

    public ProductCatalogService(ProductRepository products) {
        this.products = products;
    }

    @Cacheable(cacheNames = "products", key = "#sku")
    @Transactional(readOnly = true)
    public ProductEntity get(String sku) {
        return products.findById(sku).orElseThrow(() -> new IllegalArgumentException("Product not found"));
    }

    @CacheEvict(cacheNames = "products", key = "#sku")
    @Transactional
    public ProductEntity changePrice(String sku, BigDecimal price) {
        var product = products.findById(sku).orElseThrow(() -> new IllegalArgumentException("Product not found"));
        product.price = price;
        product.updatedAt = Instant.now();
        return products.save(product);
    }
}
