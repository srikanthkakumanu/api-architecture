package com.example.ecommerce.shipping.domain;

/**
 * Value Object: PackageWeight.
 * Enforces logistics-specific physical constraints.
 */
public record PackageWeight(double kilograms) {
    public PackageWeight {
        if (kilograms <= 0.0) {
            throw new IllegalArgumentException("Package weight must be greater than zero");
        }
        if (kilograms > 50.0) {
            throw new IllegalArgumentException("Single parcel cannot exceed carrier limit of 50.0kg");
        }
    }
}
