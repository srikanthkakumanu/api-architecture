package com.example.ecommerce.shipping.domain;

/**
 * Value Object: DeliveryAddress.
 * Enforces structured shipping address validation.
 */
public record DeliveryAddress(String street, String city, String postalCode, String country) {
    public DeliveryAddress {
        if (street == null || street.isBlank()) throw new IllegalArgumentException("Street required");
        if (city == null || city.isBlank()) throw new IllegalArgumentException("City required");
        if (postalCode == null || postalCode.isBlank()) throw new IllegalArgumentException("Postal code required");
        if (country == null || country.isBlank()) throw new IllegalArgumentException("Country required");
    }
}
