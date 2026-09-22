package com.example.ecommerce.shipping.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PackageWeightTest {

    @Test
    void rejectsZeroOrNegativeWeight() {
        assertThrows(IllegalArgumentException.class, () -> new PackageWeight(0.0));
        assertThrows(IllegalArgumentException.class, () -> new PackageWeight(-1.0));
    }

    @Test
    void rejectsWeightAboveCarrierLimit() {
        assertThrows(IllegalArgumentException.class, () -> new PackageWeight(50.01));
    }

    @Test
    void acceptsWeightWithinCarrierLimit() {
        assertEquals(50.0, new PackageWeight(50.0).kilograms());
    }
}
