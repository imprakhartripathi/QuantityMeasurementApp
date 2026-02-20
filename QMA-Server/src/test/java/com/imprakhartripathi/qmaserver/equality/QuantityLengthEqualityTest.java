package com.imprakhartripathi.qmaserver.equality;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class QuantityLengthEqualityTest {

    private static final double EPSILON = 1e-6;

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
    void testEquality_YardToYard_SameValue() {
        QuantityLengthEquality.QuantityLength first =
                new QuantityLengthEquality.QuantityLength(1.0, QuantityLengthEquality.LengthUnit.YARDS);
        QuantityLengthEquality.QuantityLength second =
                new QuantityLengthEquality.QuantityLength(1.0, QuantityLengthEquality.LengthUnit.YARDS);

        assertTrue(first.equals(second), "Expected identical yard measurements to be equal");
    }

    @Test
    void testEquality_YardToYard_DifferentValue() {
        QuantityLengthEquality.QuantityLength first =
                new QuantityLengthEquality.QuantityLength(1.0, QuantityLengthEquality.LengthUnit.YARDS);
        QuantityLengthEquality.QuantityLength second =
                new QuantityLengthEquality.QuantityLength(2.0, QuantityLengthEquality.LengthUnit.YARDS);

        assertFalse(first.equals(second), "Expected different yard measurements to be unequal");
    }

    @Test
    void testEquality_YardToFeet_EquivalentValue() {
        QuantityLengthEquality.QuantityLength yards =
                new QuantityLengthEquality.QuantityLength(1.0, QuantityLengthEquality.LengthUnit.YARDS);
        QuantityLengthEquality.QuantityLength feet =
                new QuantityLengthEquality.QuantityLength(3.0, QuantityLengthEquality.LengthUnit.FEET);

        assertTrue(yards.equals(feet), "Expected 1.0 yard to be equal to 3.0 feet");
    }

    @Test
    void testEquality_FeetToYard_EquivalentValue() {
        QuantityLengthEquality.QuantityLength feet =
                new QuantityLengthEquality.QuantityLength(3.0, QuantityLengthEquality.LengthUnit.FEET);
        QuantityLengthEquality.QuantityLength yards =
                new QuantityLengthEquality.QuantityLength(1.0, QuantityLengthEquality.LengthUnit.YARDS);

        assertTrue(feet.equals(yards), "Expected 3.0 feet to be equal to 1.0 yard");
    }

    @Test
    void testEquality_YardToInches_EquivalentValue() {
        QuantityLengthEquality.QuantityLength yards =
                new QuantityLengthEquality.QuantityLength(1.0, QuantityLengthEquality.LengthUnit.YARDS);
        QuantityLengthEquality.QuantityLength inches =
                new QuantityLengthEquality.QuantityLength(36.0, QuantityLengthEquality.LengthUnit.INCH);

        assertTrue(yards.equals(inches), "Expected 1.0 yard to be equal to 36.0 inches");
    }

    @Test
    void testEquality_InchesToYard_EquivalentValue() {
        QuantityLengthEquality.QuantityLength inches =
                new QuantityLengthEquality.QuantityLength(36.0, QuantityLengthEquality.LengthUnit.INCH);
        QuantityLengthEquality.QuantityLength yards =
                new QuantityLengthEquality.QuantityLength(1.0, QuantityLengthEquality.LengthUnit.YARDS);

        assertTrue(inches.equals(yards), "Expected 36.0 inches to be equal to 1.0 yard");
    }

    @Test
    void testEquality_YardToFeet_NonEquivalentValue() {
        QuantityLengthEquality.QuantityLength yards =
                new QuantityLengthEquality.QuantityLength(1.0, QuantityLengthEquality.LengthUnit.YARDS);
        QuantityLengthEquality.QuantityLength feet =
                new QuantityLengthEquality.QuantityLength(2.0, QuantityLengthEquality.LengthUnit.FEET);

        assertFalse(yards.equals(feet), "Expected 1.0 yard not to be equal to 2.0 feet");
    }

    @Test
    void testEquality_CentimetersToCentimeters_SameValue() {
        QuantityLengthEquality.QuantityLength first =
                new QuantityLengthEquality.QuantityLength(2.0, QuantityLengthEquality.LengthUnit.CENTIMETERS);
        QuantityLengthEquality.QuantityLength second =
                new QuantityLengthEquality.QuantityLength(2.0, QuantityLengthEquality.LengthUnit.CENTIMETERS);

        assertTrue(first.equals(second), "Expected identical centimeter measurements to be equal");
    }

    @Test
    void testEquality_CentimetersToCentimeters_DifferentValue() {
        QuantityLengthEquality.QuantityLength first =
                new QuantityLengthEquality.QuantityLength(1.0, QuantityLengthEquality.LengthUnit.CENTIMETERS);
        QuantityLengthEquality.QuantityLength second =
                new QuantityLengthEquality.QuantityLength(2.0, QuantityLengthEquality.LengthUnit.CENTIMETERS);

        assertFalse(first.equals(second), "Expected different centimeter measurements to be unequal");
    }

    @Test
    void testEquality_CentimetersToInches_EquivalentValue() {
        QuantityLengthEquality.QuantityLength centimeters =
                new QuantityLengthEquality.QuantityLength(2.54, QuantityLengthEquality.LengthUnit.CENTIMETERS);
        QuantityLengthEquality.QuantityLength inches =
                new QuantityLengthEquality.QuantityLength(1.0, QuantityLengthEquality.LengthUnit.INCH);

        assertTrue(centimeters.equals(inches), "Expected 2.54 cm to be equal to 1.0 inch");
    }

    @Test
    void testEquality_CentimetersToFeet_NonEquivalentValue() {
        QuantityLengthEquality.QuantityLength centimeters =
                new QuantityLengthEquality.QuantityLength(1.0, QuantityLengthEquality.LengthUnit.CENTIMETERS);
        QuantityLengthEquality.QuantityLength feet =
                new QuantityLengthEquality.QuantityLength(1.0, QuantityLengthEquality.LengthUnit.FEET);

        assertFalse(centimeters.equals(feet), "Expected 1.0 cm not to be equal to 1.0 ft");
    }

    @Test
    void testEquality_MultiUnit_TransitiveProperty() {
        QuantityLengthEquality.QuantityLength yards =
                new QuantityLengthEquality.QuantityLength(1.0, QuantityLengthEquality.LengthUnit.YARDS);
        QuantityLengthEquality.QuantityLength feet =
                new QuantityLengthEquality.QuantityLength(3.0, QuantityLengthEquality.LengthUnit.FEET);
        QuantityLengthEquality.QuantityLength inches =
                new QuantityLengthEquality.QuantityLength(36.0, QuantityLengthEquality.LengthUnit.INCH);

        assertTrue(yards.equals(feet), "Expected 1.0 yard to be equal to 3.0 feet");
        assertTrue(feet.equals(inches), "Expected 3.0 feet to be equal to 36.0 inches");
        assertTrue(yards.equals(inches), "Expected 1.0 yard to be equal to 36.0 inches");
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

    @Test
    void testConversion_FeetToInches() {
        double result = QuantityLengthEquality.convert(1.0,
                QuantityLengthEquality.LengthUnit.FEET, QuantityLengthEquality.LengthUnit.INCH);

        assertEquals(12.0, result, EPSILON, "Expected 1.0 ft to convert to 12.0 inches");
    }

    @Test
    void testConversion_InchesToFeet() {
        double result = QuantityLengthEquality.convert(24.0,
                QuantityLengthEquality.LengthUnit.INCH, QuantityLengthEquality.LengthUnit.FEET);

        assertEquals(2.0, result, EPSILON, "Expected 24.0 inches to convert to 2.0 feet");
    }

    @Test
    void testConversion_YardsToInches() {
        double result = QuantityLengthEquality.convert(1.0,
                QuantityLengthEquality.LengthUnit.YARDS, QuantityLengthEquality.LengthUnit.INCH);

        assertEquals(36.0, result, EPSILON, "Expected 1.0 yard to convert to 36.0 inches");
    }

    @Test
    void testConversion_InchesToYards() {
        double result = QuantityLengthEquality.convert(72.0,
                QuantityLengthEquality.LengthUnit.INCH, QuantityLengthEquality.LengthUnit.YARDS);

        assertEquals(2.0, result, EPSILON, "Expected 72.0 inches to convert to 2.0 yards");
    }

    @Test
    void testConversion_CentimetersToInches() {
        double result = QuantityLengthEquality.convert(2.54,
                QuantityLengthEquality.LengthUnit.CENTIMETERS, QuantityLengthEquality.LengthUnit.INCH);

        assertEquals(1.0, result, EPSILON, "Expected 2.54 cm to convert to 1.0 inch");
    }

    @Test
    void testConversion_FeetToYards() {
        double result = QuantityLengthEquality.convert(6.0,
                QuantityLengthEquality.LengthUnit.FEET, QuantityLengthEquality.LengthUnit.YARDS);

        assertEquals(2.0, result, EPSILON, "Expected 6.0 feet to convert to 2.0 yards");
    }

    @Test
    void testConversion_RoundTrip_PreservesValue() {
        double value = 3.5;
        double toInches = QuantityLengthEquality.convert(value,
                QuantityLengthEquality.LengthUnit.FEET, QuantityLengthEquality.LengthUnit.INCH);
        double backToFeet = QuantityLengthEquality.convert(toInches,
                QuantityLengthEquality.LengthUnit.INCH, QuantityLengthEquality.LengthUnit.FEET);

        assertEquals(value, backToFeet, EPSILON, "Expected round-trip conversion to preserve value");
    }

    @Test
    void testConversion_ZeroValue() {
        double result = QuantityLengthEquality.convert(0.0,
                QuantityLengthEquality.LengthUnit.FEET, QuantityLengthEquality.LengthUnit.INCH);

        assertEquals(0.0, result, EPSILON, "Expected zero value to remain zero after conversion");
    }

    @Test
    void testConversion_NegativeValue() {
        double result = QuantityLengthEquality.convert(-1.0,
                QuantityLengthEquality.LengthUnit.FEET, QuantityLengthEquality.LengthUnit.INCH);

        assertEquals(-12.0, result, EPSILON, "Expected negative value to preserve sign after conversion");
    }

    @Test
    void testConversion_SameUnit() {
        double result = QuantityLengthEquality.convert(5.0,
                QuantityLengthEquality.LengthUnit.FEET, QuantityLengthEquality.LengthUnit.FEET);

        assertEquals(5.0, result, EPSILON, "Expected same-unit conversion to return original value");
    }

    @Test
    void testConversion_InvalidUnit_Throws() {
        assertThrows(IllegalArgumentException.class,
                () -> QuantityLengthEquality.convert(1.0, null, QuantityLengthEquality.LengthUnit.FEET),
                "Expected null source unit to be rejected");
        assertThrows(IllegalArgumentException.class,
                () -> QuantityLengthEquality.convert(1.0, QuantityLengthEquality.LengthUnit.FEET, null),
                "Expected null target unit to be rejected");
    }

    @Test
    void testConversion_NaNOrInfinite_Throws() {
        assertThrows(IllegalArgumentException.class,
                () -> QuantityLengthEquality.convert(Double.NaN, QuantityLengthEquality.LengthUnit.FEET,
                        QuantityLengthEquality.LengthUnit.INCH),
                "Expected NaN value to be rejected");
        assertThrows(IllegalArgumentException.class,
                () -> QuantityLengthEquality.convert(Double.POSITIVE_INFINITY, QuantityLengthEquality.LengthUnit.FEET,
                        QuantityLengthEquality.LengthUnit.INCH),
                "Expected infinite value to be rejected");
    }

    @Test
    void testAddition_SameUnit_FeetPlusFeet() {
        QuantityLengthEquality.QuantityLength result = QuantityLengthEquality.add(
                new QuantityLengthEquality.QuantityLength(1.0, QuantityLengthEquality.LengthUnit.FEET),
                new QuantityLengthEquality.QuantityLength(2.0, QuantityLengthEquality.LengthUnit.FEET));

        assertEquals(3.0, result.getValue(), EPSILON, "Expected 1.0 ft + 2.0 ft = 3.0 ft");
    }

    @Test
    void testAddition_SameUnit_InchPlusInch() {
        QuantityLengthEquality.QuantityLength result = QuantityLengthEquality.add(
                new QuantityLengthEquality.QuantityLength(6.0, QuantityLengthEquality.LengthUnit.INCH),
                new QuantityLengthEquality.QuantityLength(6.0, QuantityLengthEquality.LengthUnit.INCH));

        assertEquals(12.0, result.getValue(), EPSILON, "Expected 6.0 in + 6.0 in = 12.0 in");
    }

    @Test
    void testAddition_CrossUnit_FeetPlusInches() {
        QuantityLengthEquality.QuantityLength result = QuantityLengthEquality.add(
                new QuantityLengthEquality.QuantityLength(1.0, QuantityLengthEquality.LengthUnit.FEET),
                new QuantityLengthEquality.QuantityLength(12.0, QuantityLengthEquality.LengthUnit.INCH));

        assertEquals(2.0, result.getValue(), EPSILON, "Expected 1.0 ft + 12.0 in = 2.0 ft");
    }

    @Test
    void testAddition_CrossUnit_InchPlusFeet() {
        QuantityLengthEquality.QuantityLength result = QuantityLengthEquality.add(
                new QuantityLengthEquality.QuantityLength(12.0, QuantityLengthEquality.LengthUnit.INCH),
                new QuantityLengthEquality.QuantityLength(1.0, QuantityLengthEquality.LengthUnit.FEET));

        assertEquals(24.0, result.getValue(), EPSILON, "Expected 12.0 in + 1.0 ft = 24.0 in");
    }

    @Test
    void testAddition_CrossUnit_YardPlusFeet() {
        QuantityLengthEquality.QuantityLength result = QuantityLengthEquality.add(
                new QuantityLengthEquality.QuantityLength(1.0, QuantityLengthEquality.LengthUnit.YARDS),
                new QuantityLengthEquality.QuantityLength(3.0, QuantityLengthEquality.LengthUnit.FEET));

        assertEquals(2.0, result.getValue(), EPSILON, "Expected 1.0 yd + 3.0 ft = 2.0 yd");
    }

    @Test
    void testAddition_CrossUnit_CentimeterPlusInch() {
        QuantityLengthEquality.QuantityLength result = QuantityLengthEquality.add(
                new QuantityLengthEquality.QuantityLength(2.54, QuantityLengthEquality.LengthUnit.CENTIMETERS),
                new QuantityLengthEquality.QuantityLength(1.0, QuantityLengthEquality.LengthUnit.INCH));

        assertEquals(5.08, result.getValue(), EPSILON, "Expected 2.54 cm + 1.0 in = 5.08 cm");
    }

    @Test
    void testAddition_Commutativity() {
        QuantityLengthEquality.QuantityLength first = new QuantityLengthEquality.QuantityLength(
                1.0, QuantityLengthEquality.LengthUnit.FEET);
        QuantityLengthEquality.QuantityLength second = new QuantityLengthEquality.QuantityLength(
                12.0, QuantityLengthEquality.LengthUnit.INCH);

        QuantityLengthEquality.QuantityLength resultFirst = QuantityLengthEquality.add(first, second);
        QuantityLengthEquality.QuantityLength resultSecond = QuantityLengthEquality.add(second, first);

        double firstInFeet = QuantityLengthEquality.convert(resultFirst.getValue(), resultFirst.getUnit(),
                QuantityLengthEquality.LengthUnit.FEET);
        double secondInFeet = QuantityLengthEquality.convert(resultSecond.getValue(), resultSecond.getUnit(),
                QuantityLengthEquality.LengthUnit.FEET);

        assertEquals(firstInFeet, secondInFeet, EPSILON, "Expected addition to be commutative in result value");
    }

    @Test
    void testAddition_WithZero() {
        QuantityLengthEquality.QuantityLength result = QuantityLengthEquality.add(
                new QuantityLengthEquality.QuantityLength(5.0, QuantityLengthEquality.LengthUnit.FEET),
                new QuantityLengthEquality.QuantityLength(0.0, QuantityLengthEquality.LengthUnit.INCH));

        assertEquals(5.0, result.getValue(), EPSILON, "Expected adding zero to preserve value");
    }

    @Test
    void testAddition_NegativeValues() {
        QuantityLengthEquality.QuantityLength result = QuantityLengthEquality.add(
                new QuantityLengthEquality.QuantityLength(5.0, QuantityLengthEquality.LengthUnit.FEET),
                new QuantityLengthEquality.QuantityLength(-2.0, QuantityLengthEquality.LengthUnit.FEET));

        assertEquals(3.0, result.getValue(), EPSILON, "Expected 5.0 ft + -2.0 ft = 3.0 ft");
    }

    @Test
    void testAddition_NullSecondOperand() {
        QuantityLengthEquality.QuantityLength first = new QuantityLengthEquality.QuantityLength(
                1.0, QuantityLengthEquality.LengthUnit.FEET);

        assertThrows(IllegalArgumentException.class,
                () -> QuantityLengthEquality.add(first, null),
                "Expected null operand to be rejected");
    }

    @Test
    void testAddition_LargeValues() {
        QuantityLengthEquality.QuantityLength result = QuantityLengthEquality.add(
                new QuantityLengthEquality.QuantityLength(1e6, QuantityLengthEquality.LengthUnit.FEET),
                new QuantityLengthEquality.QuantityLength(1e6, QuantityLengthEquality.LengthUnit.FEET));

        assertEquals(2e6, result.getValue(), EPSILON, "Expected large values to add correctly");
    }

    @Test
    void testAddition_SmallValues() {
        QuantityLengthEquality.QuantityLength result = QuantityLengthEquality.add(
                new QuantityLengthEquality.QuantityLength(0.001, QuantityLengthEquality.LengthUnit.FEET),
                new QuantityLengthEquality.QuantityLength(0.002, QuantityLengthEquality.LengthUnit.FEET));

        assertEquals(0.003, result.getValue(), EPSILON, "Expected small values to add correctly");
    }

}
