package com.imprakhartripathi.qmaserver.equality;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FeetEqualityTest {

    @Test
    void testEquality_SameValue() {
        FeetEquality.Feet first = new FeetEquality.Feet(1.0);
        FeetEquality.Feet second = new FeetEquality.Feet(1.0);

        assertTrue(first.equals(second), "Expected feet values with same measurement to be equal");
    }

    @Test
    void testEquality_DifferentValue() {
        FeetEquality.Feet first = new FeetEquality.Feet(1.0);
        FeetEquality.Feet second = new FeetEquality.Feet(2.0);

        assertFalse(first.equals(second), "Expected feet values with different measurements to be unequal");
    }

    @Test
    void testEquality_NullComparison() {
        FeetEquality.Feet value = new FeetEquality.Feet(1.0);

        assertFalse(value.equals(null), "Expected feet value not to be equal to null");
    }

    @Test
    void testEquality_NonNumericInput() {
        FeetEquality.Feet value = new FeetEquality.Feet(1.0);

        assertFalse(value.equals("1.0"), "Expected feet value not to be equal to a non-numeric input");
    }

    @Test
    void testEquality_SameReference() {
        FeetEquality.Feet value = new FeetEquality.Feet(1.0);

        assertTrue(value.equals(value), "Expected feet value to be equal to itself");
    }
}
