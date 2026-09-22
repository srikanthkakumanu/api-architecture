package com.example.ecommerce.shipping.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class DeliveryAddressTest {

    @Test
    void rejectsBlankStreet() {
        assertThrows(IllegalArgumentException.class,
                () -> new DeliveryAddress(" ", "London", "NW1", "UK"));
    }

    @Test
    void rejectsBlankCity() {
        assertThrows(IllegalArgumentException.class,
                () -> new DeliveryAddress("221B Baker Street", " ", "NW1", "UK"));
    }

    @Test
    void rejectsBlankPostalCode() {
        assertThrows(IllegalArgumentException.class,
                () -> new DeliveryAddress("221B Baker Street", "London", " ", "UK"));
    }

    @Test
    void rejectsBlankCountry() {
        assertThrows(IllegalArgumentException.class,
                () -> new DeliveryAddress("221B Baker Street", "London", "NW1", " "));
    }
}
