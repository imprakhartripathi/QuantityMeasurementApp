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
        Quantity<LengthUnit> first =
                new Quantity<LengthUnit>(1.0, LengthUnit.FEET);
        Quantity<LengthUnit> second =
                new Quantity<LengthUnit>(1.0, LengthUnit.FEET);

        assertTrue(first.equals(second), "Expected identical feet measurements to be equal");
    }

    @Test
    void testEquality_InchToInch_SameValue() {
        Quantity<LengthUnit> first =
                new Quantity<LengthUnit>(1.0, LengthUnit.INCH);
        Quantity<LengthUnit> second =
                new Quantity<LengthUnit>(1.0, LengthUnit.INCH);

        assertTrue(first.equals(second), "Expected identical inch measurements to be equal");
    }

    @Test
    void testEquality_FeetToInch_EquivalentValue() {
        Quantity<LengthUnit> feet =
                new Quantity<LengthUnit>(1.0, LengthUnit.FEET);
        Quantity<LengthUnit> inches =
                new Quantity<LengthUnit>(12.0, LengthUnit.INCH);

        assertTrue(feet.equals(inches), "Expected 1.0 ft to be equal to 12.0 inch");
    }

    @Test
    void testEquality_InchToFeet_EquivalentValue() {
        Quantity<LengthUnit> inches =
                new Quantity<LengthUnit>(12.0, LengthUnit.INCH);
        Quantity<LengthUnit> feet =
                new Quantity<LengthUnit>(1.0, LengthUnit.FEET);

        assertTrue(inches.equals(feet), "Expected 12.0 inch to be equal to 1.0 ft");
    }

    @Test
    void testEquality_YardToYard_SameValue() {
        Quantity<LengthUnit> first =
                new Quantity<LengthUnit>(1.0, LengthUnit.YARDS);
        Quantity<LengthUnit> second =
                new Quantity<LengthUnit>(1.0, LengthUnit.YARDS);

        assertTrue(first.equals(second), "Expected identical yard measurements to be equal");
    }

    @Test
    void testEquality_YardToYard_DifferentValue() {
        Quantity<LengthUnit> first =
                new Quantity<LengthUnit>(1.0, LengthUnit.YARDS);
        Quantity<LengthUnit> second =
                new Quantity<LengthUnit>(2.0, LengthUnit.YARDS);

        assertFalse(first.equals(second), "Expected different yard measurements to be unequal");
    }

    @Test
    void testEquality_YardToFeet_EquivalentValue() {
        Quantity<LengthUnit> yards =
                new Quantity<LengthUnit>(1.0, LengthUnit.YARDS);
        Quantity<LengthUnit> feet =
                new Quantity<LengthUnit>(3.0, LengthUnit.FEET);

        assertTrue(yards.equals(feet), "Expected 1.0 yard to be equal to 3.0 feet");
    }

    @Test
    void testEquality_FeetToYard_EquivalentValue() {
        Quantity<LengthUnit> feet =
                new Quantity<LengthUnit>(3.0, LengthUnit.FEET);
        Quantity<LengthUnit> yards =
                new Quantity<LengthUnit>(1.0, LengthUnit.YARDS);

        assertTrue(feet.equals(yards), "Expected 3.0 feet to be equal to 1.0 yard");
    }

    @Test
    void testEquality_YardToInches_EquivalentValue() {
        Quantity<LengthUnit> yards =
                new Quantity<LengthUnit>(1.0, LengthUnit.YARDS);
        Quantity<LengthUnit> inches =
                new Quantity<LengthUnit>(36.0, LengthUnit.INCH);

        assertTrue(yards.equals(inches), "Expected 1.0 yard to be equal to 36.0 inches");
    }

    @Test
    void testEquality_InchesToYard_EquivalentValue() {
        Quantity<LengthUnit> inches =
                new Quantity<LengthUnit>(36.0, LengthUnit.INCH);
        Quantity<LengthUnit> yards =
                new Quantity<LengthUnit>(1.0, LengthUnit.YARDS);

        assertTrue(inches.equals(yards), "Expected 36.0 inches to be equal to 1.0 yard");
    }

    @Test
    void testEquality_YardToFeet_NonEquivalentValue() {
        Quantity<LengthUnit> yards =
                new Quantity<LengthUnit>(1.0, LengthUnit.YARDS);
        Quantity<LengthUnit> feet =
                new Quantity<LengthUnit>(2.0, LengthUnit.FEET);

        assertFalse(yards.equals(feet), "Expected 1.0 yard not to be equal to 2.0 feet");
    }

    @Test
    void testEquality_CentimetersToCentimeters_SameValue() {
        Quantity<LengthUnit> first =
                new Quantity<LengthUnit>(2.0, LengthUnit.CENTIMETERS);
        Quantity<LengthUnit> second =
                new Quantity<LengthUnit>(2.0, LengthUnit.CENTIMETERS);

        assertTrue(first.equals(second), "Expected identical centimeter measurements to be equal");
    }

    @Test
    void testEquality_CentimetersToCentimeters_DifferentValue() {
        Quantity<LengthUnit> first =
                new Quantity<LengthUnit>(1.0, LengthUnit.CENTIMETERS);
        Quantity<LengthUnit> second =
                new Quantity<LengthUnit>(2.0, LengthUnit.CENTIMETERS);

        assertFalse(first.equals(second), "Expected different centimeter measurements to be unequal");
    }

    @Test
    void testEquality_CentimetersToInches_EquivalentValue() {
        Quantity<LengthUnit> centimeters =
                new Quantity<LengthUnit>(2.54, LengthUnit.CENTIMETERS);
        Quantity<LengthUnit> inches =
                new Quantity<LengthUnit>(1.0, LengthUnit.INCH);

        assertTrue(centimeters.equals(inches), "Expected 2.54 cm to be equal to 1.0 inch");
    }

    @Test
    void testEquality_CentimetersToFeet_NonEquivalentValue() {
        Quantity<LengthUnit> centimeters =
                new Quantity<LengthUnit>(1.0, LengthUnit.CENTIMETERS);
        Quantity<LengthUnit> feet =
                new Quantity<LengthUnit>(1.0, LengthUnit.FEET);

        assertFalse(centimeters.equals(feet), "Expected 1.0 cm not to be equal to 1.0 ft");
    }

    @Test
    void testEquality_MultiUnit_TransitiveProperty() {
        Quantity<LengthUnit> yards =
                new Quantity<LengthUnit>(1.0, LengthUnit.YARDS);
        Quantity<LengthUnit> feet =
                new Quantity<LengthUnit>(3.0, LengthUnit.FEET);
        Quantity<LengthUnit> inches =
                new Quantity<LengthUnit>(36.0, LengthUnit.INCH);

        assertTrue(yards.equals(feet), "Expected 1.0 yard to be equal to 3.0 feet");
        assertTrue(feet.equals(inches), "Expected 3.0 feet to be equal to 36.0 inches");
        assertTrue(yards.equals(inches), "Expected 1.0 yard to be equal to 36.0 inches");
    }

    @Test
    void testEquality_FeetToFeet_DifferentValue() {
        Quantity<LengthUnit> first =
                new Quantity<LengthUnit>(1.0, LengthUnit.FEET);
        Quantity<LengthUnit> second =
                new Quantity<LengthUnit>(2.0, LengthUnit.FEET);

        assertFalse(first.equals(second), "Expected different feet measurements to be unequal");
    }

    @Test
    void testEquality_InchToInch_DifferentValue() {
        Quantity<LengthUnit> first =
                new Quantity<LengthUnit>(1.0, LengthUnit.INCH);
        Quantity<LengthUnit> second =
                new Quantity<LengthUnit>(2.0, LengthUnit.INCH);

        assertFalse(first.equals(second), "Expected different inch measurements to be unequal");
    }

    @Test
    void testEquality_NullComparison() {
        Quantity<LengthUnit> value =
                new Quantity<LengthUnit>(1.0, LengthUnit.FEET);

        assertFalse(value.equals(null), "Expected quantity not to be equal to null");
    }

    @Test
    void testEquality_SameReference() {
        Quantity<LengthUnit> value =
                new Quantity<LengthUnit>(1.0, LengthUnit.FEET);

        assertTrue(value.equals(value), "Expected quantity to be equal to itself");
    }

    @Test
    void testEquality_InvalidUnit() {
        assertThrows(IllegalArgumentException.class,
                () -> new Quantity<LengthUnit>(1.0, null),
                "Expected null unit to be rejected");
    }

    @Test
    void testEquality_NonNumericInput() {
        assertThrows(IllegalArgumentException.class,
                () -> new Quantity<LengthUnit>(Double.NaN, LengthUnit.FEET),
                "Expected NaN value to be rejected");
    }

    @Test
    void testConversion_FeetToInches() {
        double result = QuantityLengthEquality.convert(1.0,
                LengthUnit.FEET, LengthUnit.INCH);

        assertEquals(12.0, result, EPSILON, "Expected 1.0 ft to convert to 12.0 inches");
    }

    @Test
    void testConversion_InchesToFeet() {
        double result = QuantityLengthEquality.convert(24.0,
                LengthUnit.INCH, LengthUnit.FEET);

        assertEquals(2.0, result, EPSILON, "Expected 24.0 inches to convert to 2.0 feet");
    }

    @Test
    void testConversion_YardsToInches() {
        double result = QuantityLengthEquality.convert(1.0,
                LengthUnit.YARDS, LengthUnit.INCH);

        assertEquals(36.0, result, EPSILON, "Expected 1.0 yard to convert to 36.0 inches");
    }

    @Test
    void testConversion_InchesToYards() {
        double result = QuantityLengthEquality.convert(72.0,
                LengthUnit.INCH, LengthUnit.YARDS);

        assertEquals(2.0, result, EPSILON, "Expected 72.0 inches to convert to 2.0 yards");
    }

    @Test
    void testConversion_CentimetersToInches() {
        double result = QuantityLengthEquality.convert(2.54,
                LengthUnit.CENTIMETERS, LengthUnit.INCH);

        assertEquals(1.0, result, EPSILON, "Expected 2.54 cm to convert to 1.0 inch");
    }

    @Test
    void testConversion_FeetToYards() {
        double result = QuantityLengthEquality.convert(6.0,
                LengthUnit.FEET, LengthUnit.YARDS);

        assertEquals(2.0, result, EPSILON, "Expected 6.0 feet to convert to 2.0 yards");
    }

    @Test
    void testConversion_RoundTrip_PreservesValue() {
        double value = 3.5;
        double toInches = QuantityLengthEquality.convert(value,
                LengthUnit.FEET, LengthUnit.INCH);
        double backToFeet = QuantityLengthEquality.convert(toInches,
                LengthUnit.INCH, LengthUnit.FEET);

        assertEquals(value, backToFeet, EPSILON, "Expected round-trip conversion to preserve value");
    }

    @Test
    void testConversion_ZeroValue() {
        double result = QuantityLengthEquality.convert(0.0,
                LengthUnit.FEET, LengthUnit.INCH);

        assertEquals(0.0, result, EPSILON, "Expected zero value to remain zero after conversion");
    }

    @Test
    void testConversion_NegativeValue() {
        double result = QuantityLengthEquality.convert(-1.0,
                LengthUnit.FEET, LengthUnit.INCH);

        assertEquals(-12.0, result, EPSILON, "Expected negative value to preserve sign after conversion");
    }

    @Test
    void testConversion_SameUnit() {
        double result = QuantityLengthEquality.convert(5.0,
                LengthUnit.FEET, LengthUnit.FEET);

        assertEquals(5.0, result, EPSILON, "Expected same-unit conversion to return original value");
    }

    @Test
    void testConversion_InvalidUnit_Throws() {
        assertThrows(IllegalArgumentException.class,
                () -> QuantityLengthEquality.convert(1.0, null, LengthUnit.FEET),
                "Expected null source unit to be rejected");
        assertThrows(IllegalArgumentException.class,
                () -> QuantityLengthEquality.convert(1.0, LengthUnit.FEET, null),
                "Expected null target unit to be rejected");
    }

    @Test
    void testConversion_NaNOrInfinite_Throws() {
        assertThrows(IllegalArgumentException.class,
                () -> QuantityLengthEquality.convert(Double.NaN, LengthUnit.FEET,
                        LengthUnit.INCH),
                "Expected NaN value to be rejected");
        assertThrows(IllegalArgumentException.class,
                () -> QuantityLengthEquality.convert(Double.POSITIVE_INFINITY, LengthUnit.FEET,
                        LengthUnit.INCH),
                "Expected infinite value to be rejected");
    }

    @Test
    void testAddition_SameUnit_FeetPlusFeet() {
        Quantity<LengthUnit> result = QuantityLengthEquality.add(
                new Quantity<LengthUnit>(1.0, LengthUnit.FEET),
                new Quantity<LengthUnit>(2.0, LengthUnit.FEET));

        assertEquals(3.0, result.getValue(), EPSILON, "Expected 1.0 ft + 2.0 ft = 3.0 ft");
    }

    @Test
    void testAddition_SameUnit_InchPlusInch() {
        Quantity<LengthUnit> result = QuantityLengthEquality.add(
                new Quantity<LengthUnit>(6.0, LengthUnit.INCH),
                new Quantity<LengthUnit>(6.0, LengthUnit.INCH));

        assertEquals(12.0, result.getValue(), EPSILON, "Expected 6.0 in + 6.0 in = 12.0 in");
    }

    @Test
    void testAddition_CrossUnit_FeetPlusInches() {
        Quantity<LengthUnit> result = QuantityLengthEquality.add(
                new Quantity<LengthUnit>(1.0, LengthUnit.FEET),
                new Quantity<LengthUnit>(12.0, LengthUnit.INCH));

        assertEquals(2.0, result.getValue(), EPSILON, "Expected 1.0 ft + 12.0 in = 2.0 ft");
    }

    @Test
    void testAddition_CrossUnit_InchPlusFeet() {
        Quantity<LengthUnit> result = QuantityLengthEquality.add(
                new Quantity<LengthUnit>(12.0, LengthUnit.INCH),
                new Quantity<LengthUnit>(1.0, LengthUnit.FEET));

        assertEquals(24.0, result.getValue(), EPSILON, "Expected 12.0 in + 1.0 ft = 24.0 in");
    }

    @Test
    void testAddition_CrossUnit_YardPlusFeet() {
        Quantity<LengthUnit> result = QuantityLengthEquality.add(
                new Quantity<LengthUnit>(1.0, LengthUnit.YARDS),
                new Quantity<LengthUnit>(3.0, LengthUnit.FEET));

        assertEquals(2.0, result.getValue(), EPSILON, "Expected 1.0 yd + 3.0 ft = 2.0 yd");
    }

    @Test
    void testAddition_CrossUnit_CentimeterPlusInch() {
        Quantity<LengthUnit> result = QuantityLengthEquality.add(
                new Quantity<LengthUnit>(2.54, LengthUnit.CENTIMETERS),
                new Quantity<LengthUnit>(1.0, LengthUnit.INCH));

        assertEquals(5.08, result.getValue(), EPSILON, "Expected 2.54 cm + 1.0 in = 5.08 cm");
    }

    @Test
    void testAddition_Commutativity() {
        Quantity<LengthUnit> first = new Quantity<LengthUnit>(
                1.0, LengthUnit.FEET);
        Quantity<LengthUnit> second = new Quantity<LengthUnit>(
                12.0, LengthUnit.INCH);

        Quantity<LengthUnit> resultFirst = QuantityLengthEquality.add(first, second);
        Quantity<LengthUnit> resultSecond = QuantityLengthEquality.add(second, first);

        double firstInFeet = QuantityLengthEquality.convert(resultFirst.getValue(), resultFirst.getUnit(),
                LengthUnit.FEET);
        double secondInFeet = QuantityLengthEquality.convert(resultSecond.getValue(), resultSecond.getUnit(),
                LengthUnit.FEET);

        assertEquals(firstInFeet, secondInFeet, EPSILON, "Expected addition to be commutative in result value");
    }

    @Test
    void testAddition_WithZero() {
        Quantity<LengthUnit> result = QuantityLengthEquality.add(
                new Quantity<LengthUnit>(5.0, LengthUnit.FEET),
                new Quantity<LengthUnit>(0.0, LengthUnit.INCH));

        assertEquals(5.0, result.getValue(), EPSILON, "Expected adding zero to preserve value");
    }

    @Test
    void testAddition_NegativeValues() {
        Quantity<LengthUnit> result = QuantityLengthEquality.add(
                new Quantity<LengthUnit>(5.0, LengthUnit.FEET),
                new Quantity<LengthUnit>(-2.0, LengthUnit.FEET));

        assertEquals(3.0, result.getValue(), EPSILON, "Expected 5.0 ft + -2.0 ft = 3.0 ft");
    }

    @Test
    void testAddition_NullSecondOperand() {
        Quantity<LengthUnit> first = new Quantity<LengthUnit>(
                1.0, LengthUnit.FEET);

        assertThrows(IllegalArgumentException.class,
                () -> QuantityLengthEquality.add(first, null),
                "Expected null operand to be rejected");
    }

    @Test
    void testAddition_LargeValues() {
        Quantity<LengthUnit> result = QuantityLengthEquality.add(
                new Quantity<LengthUnit>(1e6, LengthUnit.FEET),
                new Quantity<LengthUnit>(1e6, LengthUnit.FEET));

        assertEquals(2e6, result.getValue(), EPSILON, "Expected large values to add correctly");
    }

    @Test
    void testAddition_SmallValues() {
        Quantity<LengthUnit> result = QuantityLengthEquality.add(
                new Quantity<LengthUnit>(0.001, LengthUnit.FEET),
                new Quantity<LengthUnit>(0.002, LengthUnit.FEET));

        assertEquals(0.003, result.getValue(), EPSILON, "Expected small values to add correctly");
    }

    @Test
    void testAddition_ExplicitTargetUnit_Feet() {
        Quantity<LengthUnit> result = QuantityLengthEquality.add(
                new Quantity<LengthUnit>(1.0, LengthUnit.FEET),
                new Quantity<LengthUnit>(12.0, LengthUnit.INCH),
                LengthUnit.FEET);

        assertEquals(2.0, result.getValue(), EPSILON, "Expected explicit target unit feet result");
        assertEquals(LengthUnit.FEET, result.getUnit(),
                "Expected result unit to be feet");
    }

    @Test
    void testAddition_ExplicitTargetUnit_Inches() {
        Quantity<LengthUnit> result = QuantityLengthEquality.add(
                new Quantity<LengthUnit>(1.0, LengthUnit.FEET),
                new Quantity<LengthUnit>(12.0, LengthUnit.INCH),
                LengthUnit.INCH);

        assertEquals(24.0, result.getValue(), EPSILON, "Expected explicit target unit inches result");
        assertEquals(LengthUnit.INCH, result.getUnit(),
                "Expected result unit to be inches");
    }

    @Test
    void testAddition_ExplicitTargetUnit_Yards() {
        Quantity<LengthUnit> result = QuantityLengthEquality.add(
                new Quantity<LengthUnit>(1.0, LengthUnit.FEET),
                new Quantity<LengthUnit>(12.0, LengthUnit.INCH),
                LengthUnit.YARDS);

        assertEquals(2.0 / 3.0, result.getValue(), EPSILON, "Expected explicit target unit yards result");
        assertEquals(LengthUnit.YARDS, result.getUnit(),
                "Expected result unit to be yards");
    }

    @Test
    void testAddition_ExplicitTargetUnit_Centimeters() {
        Quantity<LengthUnit> result = QuantityLengthEquality.add(
                new Quantity<LengthUnit>(1.0, LengthUnit.INCH),
                new Quantity<LengthUnit>(1.0, LengthUnit.INCH),
                LengthUnit.CENTIMETERS);

        assertEquals(5.08, result.getValue(), EPSILON, "Expected explicit target unit centimeters result");
        assertEquals(LengthUnit.CENTIMETERS, result.getUnit(),
                "Expected result unit to be centimeters");
    }

    @Test
    void testAddition_ExplicitTargetUnit_SameAsFirstOperand() {
        Quantity<LengthUnit> result = QuantityLengthEquality.add(
                new Quantity<LengthUnit>(2.0, LengthUnit.YARDS),
                new Quantity<LengthUnit>(3.0, LengthUnit.FEET),
                LengthUnit.YARDS);

        assertEquals(3.0, result.getValue(), EPSILON, "Expected result in yards");
        assertEquals(LengthUnit.YARDS, result.getUnit(),
                "Expected result unit to be yards");
    }

    @Test
    void testAddition_ExplicitTargetUnit_SameAsSecondOperand() {
        Quantity<LengthUnit> result = QuantityLengthEquality.add(
                new Quantity<LengthUnit>(2.0, LengthUnit.YARDS),
                new Quantity<LengthUnit>(3.0, LengthUnit.FEET),
                LengthUnit.FEET);

        assertEquals(9.0, result.getValue(), EPSILON, "Expected result in feet");
        assertEquals(LengthUnit.FEET, result.getUnit(),
                "Expected result unit to be feet");
    }

    @Test
    void testAddition_ExplicitTargetUnit_Commutativity() {
        Quantity<LengthUnit> first = new Quantity<LengthUnit>(
                1.0, LengthUnit.FEET);
        Quantity<LengthUnit> second = new Quantity<LengthUnit>(
                12.0, LengthUnit.INCH);

        Quantity<LengthUnit> resultFirst = QuantityLengthEquality.add(
                first, second, LengthUnit.YARDS);
        Quantity<LengthUnit> resultSecond = QuantityLengthEquality.add(
                second, first, LengthUnit.YARDS);

        assertEquals(resultFirst.getValue(), resultSecond.getValue(), EPSILON,
                "Expected explicit-target addition to be commutative");
    }

    @Test
    void testAddition_ExplicitTargetUnit_WithZero() {
        Quantity<LengthUnit> result = QuantityLengthEquality.add(
                new Quantity<LengthUnit>(5.0, LengthUnit.FEET),
                new Quantity<LengthUnit>(0.0, LengthUnit.INCH),
                LengthUnit.YARDS);

        assertEquals(5.0 / 3.0, result.getValue(), EPSILON, "Expected result in yards with zero operand");
        assertEquals(LengthUnit.YARDS, result.getUnit(),
                "Expected result unit to be yards");
    }

    @Test
    void testAddition_ExplicitTargetUnit_NegativeValues() {
        Quantity<LengthUnit> result = QuantityLengthEquality.add(
                new Quantity<LengthUnit>(5.0, LengthUnit.FEET),
                new Quantity<LengthUnit>(-2.0, LengthUnit.FEET),
                LengthUnit.INCH);

        assertEquals(36.0, result.getValue(), EPSILON, "Expected negative values to add correctly");
        assertEquals(LengthUnit.INCH, result.getUnit(),
                "Expected result unit to be inches");
    }

    @Test
    void testAddition_ExplicitTargetUnit_NullTargetUnit() {
        assertThrows(IllegalArgumentException.class,
                () -> QuantityLengthEquality.add(
                        new Quantity<LengthUnit>(1.0, LengthUnit.FEET),
                        new Quantity<LengthUnit>(12.0, LengthUnit.INCH),
                        null),
                "Expected null target unit to be rejected");
    }

    @Test
    void testAddition_ExplicitTargetUnit_LargeToSmallScale() {
        Quantity<LengthUnit> result = QuantityLengthEquality.add(
                new Quantity<LengthUnit>(1000.0, LengthUnit.FEET),
                new Quantity<LengthUnit>(500.0, LengthUnit.FEET),
                LengthUnit.INCH);

        assertEquals(18000.0, result.getValue(), EPSILON, "Expected large values to convert to inches");
    }

    @Test
    void testAddition_ExplicitTargetUnit_SmallToLargeScale() {
        Quantity<LengthUnit> result = QuantityLengthEquality.add(
                new Quantity<LengthUnit>(12.0, LengthUnit.INCH),
                new Quantity<LengthUnit>(12.0, LengthUnit.INCH),
                LengthUnit.YARDS);

        assertEquals(2.0 / 3.0, result.getValue(), EPSILON, "Expected inches to convert to yards");
    }

}
