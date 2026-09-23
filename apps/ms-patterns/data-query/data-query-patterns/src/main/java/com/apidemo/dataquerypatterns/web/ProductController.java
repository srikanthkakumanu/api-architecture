package com.apidemo.dataquerypatterns.web;

import com.apidemo.dataquerypatterns.cache.ProductCatalogService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductCatalogService products;

    public ProductController(ProductCatalogService products) {
        this.products = products;
    }

    @GetMapping("/{sku}")
    public RestDtos.ProductResponse get(@PathVariable String sku) {
        return RestDtos.ProductResponse.from(products.get(sku));
    }

    @PatchMapping("/{sku}/price")
    public RestDtos.ProductResponse changePrice(@PathVariable String sku, @Valid @RequestBody RestDtos.ChangePriceRequest request) {
        return RestDtos.ProductResponse.from(products.changePrice(sku, request.price()));
    }
}
