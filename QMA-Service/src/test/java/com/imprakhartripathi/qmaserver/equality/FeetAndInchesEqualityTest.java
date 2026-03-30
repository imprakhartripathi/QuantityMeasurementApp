package com.imprakhartripathi.qmaserver.equality;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FeetAndInchesEqualityTest {

    @Test
    void testEquality_SameValue_Feet() {
        FeetAndInchesEquality.Feet first = new FeetAndInchesEquality.Feet(1.0);
        FeetAndInchesEquality.Feet second = new FeetAndInchesEquality.Feet(1.0);

        assertTrue(first.equals(second), "Expected feet values with same measurement to be equal");
    }

    @Test
    void testEquality_DifferentValue_Feet() {
        FeetAndInchesEquality.Feet first = new FeetAndInchesEquality.Feet(1.0);
        FeetAndInchesEquality.Feet second = new FeetAndInchesEquality.Feet(2.0);

        assertFalse(first.equals(second), "Expected feet values with different measurements to be unequal");
    }

    @Test
    void testEquality_NullComparison_Feet() {
        FeetAndInchesEquality.Feet value = new FeetAndInchesEquality.Feet(1.0);

        assertFalse(value.equals(null), "Expected feet value not to be equal to null");
    }

    @Test
    void testEquality_NonNumericInput_Feet() {
        FeetAndInchesEquality.Feet value = new FeetAndInchesEquality.Feet(1.0);

        assertFalse(value.equals("1.0"), "Expected feet value not to be equal to a non-numeric input");
    }

    @Test
    void testEquality_SameReference_Feet() {
        FeetAndInchesEquality.Feet value = new FeetAndInchesEquality.Feet(1.0);

        assertTrue(value.equals(value), "Expected feet value to be equal to itself");
    }

    @Test
    void testEquality_SameValue_Inches() {
        FeetAndInchesEquality.Inches first = new FeetAndInchesEquality.Inches(1.0);
        FeetAndInchesEquality.Inches second = new FeetAndInchesEquality.Inches(1.0);

        assertTrue(first.equals(second), "Expected inches values with same measurement to be equal");
    }

    @Test
    void testEquality_DifferentValue_Inches() {
        FeetAndInchesEquality.Inches first = new FeetAndInchesEquality.Inches(1.0);
        FeetAndInchesEquality.Inches second = new FeetAndInchesEquality.Inches(2.0);

        assertFalse(first.equals(second), "Expected inches values with different measurements to be unequal");
    }

    @Test
    void testEquality_NullComparison_Inches() {
        FeetAndInchesEquality.Inches value = new FeetAndInchesEquality.Inches(1.0);

        assertFalse(value.equals(null), "Expected inches value not to be equal to null");
    }

    @Test
    void testEquality_NonNumericInput_Inches() {
        FeetAndInchesEquality.Inches value = new FeetAndInchesEquality.Inches(1.0);

        assertFalse(value.equals("1.0"), "Expected inches value not to be equal to a non-numeric input");
    }

    @Test
    void testEquality_SameReference_Inches() {
        FeetAndInchesEquality.Inches value = new FeetAndInchesEquality.Inches(1.0);

        assertTrue(value.equals(value), "Expected inches value to be equal to itself");
    }
}
