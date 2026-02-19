package com.imprakhartripathi.qmaserver.equality;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class QuantityLengthEqualityTest {

    @Test
    void testEquality_FeetToFeet_SameValue() {
        QuantityLengthEquality.QuantityLength first =
                new QuantityLengthEquality.QuantityLength(1.0, QuantityLengthEquality.LengthUnit.FEET);
        QuantityLengthEquality.QuantityLength second =
                new QuantityLengthEquality.QuantityLength(1.0, QuantityLengthEquality.LengthUnit.FEET);

        assertTrue(first.equals(second), "Expected identical feet measurements to be equal");
    }

    @Test
    void testEquality_InchToInch_SameValue() {
        QuantityLengthEquality.QuantityLength first =
                new QuantityLengthEquality.QuantityLength(1.0, QuantityLengthEquality.LengthUnit.INCH);
        QuantityLengthEquality.QuantityLength second =
                new QuantityLengthEquality.QuantityLength(1.0, QuantityLengthEquality.LengthUnit.INCH);

        assertTrue(first.equals(second), "Expected identical inch measurements to be equal");
    }

    @Test
    void testEquality_FeetToInch_EquivalentValue() {
        QuantityLengthEquality.QuantityLength feet =
                new QuantityLengthEquality.QuantityLength(1.0, QuantityLengthEquality.LengthUnit.FEET);
        QuantityLengthEquality.QuantityLength inches =
                new QuantityLengthEquality.QuantityLength(12.0, QuantityLengthEquality.LengthUnit.INCH);

        assertTrue(feet.equals(inches), "Expected 1.0 ft to be equal to 12.0 inch");
    }

    @Test
    void testEquality_InchToFeet_EquivalentValue() {
        QuantityLengthEquality.QuantityLength inches =
                new QuantityLengthEquality.QuantityLength(12.0, QuantityLengthEquality.LengthUnit.INCH);
        QuantityLengthEquality.QuantityLength feet =
                new QuantityLengthEquality.QuantityLength(1.0, QuantityLengthEquality.LengthUnit.FEET);

        assertTrue(inches.equals(feet), "Expected 12.0 inch to be equal to 1.0 ft");
    }

    @Test
    void testEquality_FeetToFeet_DifferentValue() {
        QuantityLengthEquality.QuantityLength first =
                new QuantityLengthEquality.QuantityLength(1.0, QuantityLengthEquality.LengthUnit.FEET);
        QuantityLengthEquality.QuantityLength second =
                new QuantityLengthEquality.QuantityLength(2.0, QuantityLengthEquality.LengthUnit.FEET);

        assertFalse(first.equals(second), "Expected different feet measurements to be unequal");
    }

    @Test
    void testEquality_InchToInch_DifferentValue() {
        QuantityLengthEquality.QuantityLength first =
                new QuantityLengthEquality.QuantityLength(1.0, QuantityLengthEquality.LengthUnit.INCH);
        QuantityLengthEquality.QuantityLength second =
                new QuantityLengthEquality.QuantityLength(2.0, QuantityLengthEquality.LengthUnit.INCH);

        assertFalse(first.equals(second), "Expected different inch measurements to be unequal");
    }

    @Test
    void testEquality_NullComparison() {
        QuantityLengthEquality.QuantityLength value =
                new QuantityLengthEquality.QuantityLength(1.0, QuantityLengthEquality.LengthUnit.FEET);

        assertFalse(value.equals(null), "Expected quantity not to be equal to null");
    }

    @Test
    void testEquality_SameReference() {
        QuantityLengthEquality.QuantityLength value =
                new QuantityLengthEquality.QuantityLength(1.0, QuantityLengthEquality.LengthUnit.FEET);

        assertTrue(value.equals(value), "Expected quantity to be equal to itself");
    }

    @Test
    void testEquality_InvalidUnit() {
        assertThrows(IllegalArgumentException.class,
                () -> new QuantityLengthEquality.QuantityLength(1.0, null),
                "Expected null unit to be rejected");
    }

    @Test
    void testEquality_NonNumericInput() {
        assertThrows(IllegalArgumentException.class,
                () -> new QuantityLengthEquality.QuantityLength(Double.NaN, QuantityLengthEquality.LengthUnit.FEET),
                "Expected NaN value to be rejected");
    }
}
