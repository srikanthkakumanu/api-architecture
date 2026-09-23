package com.apidemo.dataquerypatterns.cache;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "products")
public class ProductEntity {
    @Id
    public String sku;
    public String name;
    public BigDecimal price;
    public String currency;
    public Instant updatedAt;
}
