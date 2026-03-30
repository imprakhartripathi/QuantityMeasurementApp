package com.imprakhartripathi.qmaserver.equality;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class QuantityWeightEqualityTest {

    private static final double EPSILON = 1e-6;

    @Test
    void testEquality_KilogramToKilogram_SameValue() {
        Quantity<WeightUnit> first =
                new Quantity<WeightUnit>(1.0, WeightUnit.KILOGRAM);
        Quantity<WeightUnit> second =
                new Quantity<WeightUnit>(1.0, WeightUnit.KILOGRAM);

        assertTrue(first.equals(second), "Expected identical kilogram measurements to be equal");
    }

    @Test
    void testEquality_KilogramToKilogram_DifferentValue() {
        Quantity<WeightUnit> first =
                new Quantity<WeightUnit>(1.0, WeightUnit.KILOGRAM);
        Quantity<WeightUnit> second =
                new Quantity<WeightUnit>(2.0, WeightUnit.KILOGRAM);

        assertFalse(first.equals(second), "Expected different kilogram measurements to be unequal");
    }

    @Test
    void testEquality_KilogramToGram_EquivalentValue() {
        Quantity<WeightUnit> kilograms =
                new Quantity<WeightUnit>(1.0, WeightUnit.KILOGRAM);
        Quantity<WeightUnit> grams =
                new Quantity<WeightUnit>(1000.0, WeightUnit.GRAM);

        assertTrue(kilograms.equals(grams), "Expected 1.0 kg to be equal to 1000.0 g");
    }

    @Test
    void testEquality_GramToKilogram_EquivalentValue() {
        Quantity<WeightUnit> grams =
                new Quantity<WeightUnit>(1000.0, WeightUnit.GRAM);
        Quantity<WeightUnit> kilograms =
                new Quantity<WeightUnit>(1.0, WeightUnit.KILOGRAM);

        assertTrue(grams.equals(kilograms), "Expected 1000.0 g to be equal to 1.0 kg");
    }

    @Test
    void testEquality_PoundToKilogram_EquivalentValue() {
        Quantity<WeightUnit> pounds =
                new Quantity<WeightUnit>(1.0, WeightUnit.POUND);
        Quantity<WeightUnit> kilograms =
                new Quantity<WeightUnit>(0.45359237, WeightUnit.KILOGRAM);

        assertTrue(pounds.equals(kilograms), "Expected 1.0 lb to be equal to 0.453592 kg");
    }

    @Test
    void testEquality_PoundToGram_EquivalentValue() {
        Quantity<WeightUnit> pounds =
                new Quantity<WeightUnit>(1.0, WeightUnit.POUND);
        Quantity<WeightUnit> grams =
                new Quantity<WeightUnit>(453.59237, WeightUnit.GRAM);

        assertTrue(pounds.equals(grams), "Expected 1.0 lb to be equal to 453.592 g");
    }

    @Test
    void testEquality_WeightVsLength_Incompatible() {
        Quantity<WeightUnit> weight =
                new Quantity<WeightUnit>(1.0, WeightUnit.KILOGRAM);
        Quantity<LengthUnit> length =
                new Quantity<LengthUnit>(1.0, LengthUnit.FEET);

        assertFalse(weight.equals(length), "Expected weight not to be equal to length");
    }

    @Test
    void testEquality_NullComparison() {
        Quantity<WeightUnit> weight =
                new Quantity<WeightUnit>(1.0, WeightUnit.KILOGRAM);

        assertFalse(weight.equals(null), "Expected weight not to be equal to null");
    }

    @Test
    void testEquality_SameReference() {
        Quantity<WeightUnit> weight =
                new Quantity<WeightUnit>(1.0, WeightUnit.KILOGRAM);

        assertTrue(weight.equals(weight), "Expected weight to be equal to itself");
    }

    @Test
    void testEquality_NullUnit() {
        assertThrows(IllegalArgumentException.class,
                () -> new Quantity<WeightUnit>(1.0, null),
                "Expected null unit to be rejected");
    }

    @Test
    void testEquality_ZeroValue() {
        Quantity<WeightUnit> kilograms =
                new Quantity<WeightUnit>(0.0, WeightUnit.KILOGRAM);
        Quantity<WeightUnit> grams =
                new Quantity<WeightUnit>(0.0, WeightUnit.GRAM);

        assertTrue(kilograms.equals(grams), "Expected zero values to be equal across units");
    }

    @Test
    void testEquality_NegativeWeight() {
        Quantity<WeightUnit> kilograms =
                new Quantity<WeightUnit>(-1.0, WeightUnit.KILOGRAM);
        Quantity<WeightUnit> grams =
                new Quantity<WeightUnit>(-1000.0, WeightUnit.GRAM);

        assertTrue(kilograms.equals(grams), "Expected negative values to convert correctly");
    }

    @Test
    void testEquality_LargeWeightValue() {
        Quantity<WeightUnit> grams =
                new Quantity<WeightUnit>(1_000_000.0, WeightUnit.GRAM);
        Quantity<WeightUnit> kilograms =
                new Quantity<WeightUnit>(1000.0, WeightUnit.KILOGRAM);

        assertTrue(grams.equals(kilograms), "Expected large values to convert correctly");
    }

    @Test
    void testEquality_SmallWeightValue() {
        Quantity<WeightUnit> kilograms =
                new Quantity<WeightUnit>(0.001, WeightUnit.KILOGRAM);
        Quantity<WeightUnit> grams =
                new Quantity<WeightUnit>(1.0, WeightUnit.GRAM);

        assertTrue(kilograms.equals(grams), "Expected small values to convert correctly");
    }

    @Test
    void testConversion_PoundToKilogram() {
        Quantity<WeightUnit> result =
                new Quantity<WeightUnit>(2.0, WeightUnit.POUND)
                        .convertTo(WeightUnit.KILOGRAM);

        assertEquals(0.907184, result.getValue(), EPSILON, "Expected 2.0 lb to convert to kg");
    }

    @Test
    void testConversion_KilogramToPound() {
        Quantity<WeightUnit> result =
                new Quantity<WeightUnit>(1.0, WeightUnit.KILOGRAM)
                        .convertTo(WeightUnit.POUND);

        assertEquals(2.2046226218, result.getValue(), EPSILON, "Expected 1.0 kg to convert to lb");
    }

    @Test
    void testConversion_SameUnit() {
        Quantity<WeightUnit> result =
                new Quantity<WeightUnit>(5.0, WeightUnit.KILOGRAM)
                        .convertTo(WeightUnit.KILOGRAM);

        assertEquals(5.0, result.getValue(), EPSILON, "Expected same-unit conversion to preserve value");
    }

    @Test
    void testConversion_ZeroValue() {
        Quantity<WeightUnit> result =
                new Quantity<WeightUnit>(0.0, WeightUnit.KILOGRAM)
                        .convertTo(WeightUnit.GRAM);

        assertEquals(0.0, result.getValue(), EPSILON, "Expected zero conversion to be zero");
    }

    @Test
    void testConversion_NegativeValue() {
        Quantity<WeightUnit> result =
                new Quantity<WeightUnit>(-1.0, WeightUnit.KILOGRAM)
                        .convertTo(WeightUnit.GRAM);

        assertEquals(-1000.0, result.getValue(), EPSILON, "Expected negative conversion to preserve sign");
    }

    @Test
    void testConversion_RoundTrip() {
        Quantity<WeightUnit> result =
                new Quantity<WeightUnit>(1.5, WeightUnit.KILOGRAM)
                        .convertTo(WeightUnit.GRAM)
                        .convertTo(WeightUnit.KILOGRAM);

        assertEquals(1.5, result.getValue(), EPSILON, "Expected round-trip conversion to preserve value");
    }

    @Test
    void testAddition_SameUnit_KilogramPlusKilogram() {
        Quantity<WeightUnit> result = QuantityWeightEquality.add(
                new Quantity<WeightUnit>(1.0, WeightUnit.KILOGRAM),
                new Quantity<WeightUnit>(2.0, WeightUnit.KILOGRAM));

        assertEquals(3.0, result.getValue(), EPSILON, "Expected 1.0 kg + 2.0 kg = 3.0 kg");
    }

    @Test
    void testAddition_CrossUnit_KilogramPlusGram() {
        Quantity<WeightUnit> result = QuantityWeightEquality.add(
                new Quantity<WeightUnit>(1.0, WeightUnit.KILOGRAM),
                new Quantity<WeightUnit>(1000.0, WeightUnit.GRAM));

        assertEquals(2.0, result.getValue(), EPSILON, "Expected 1.0 kg + 1000.0 g = 2.0 kg");
    }

    @Test
    void testAddition_CrossUnit_PoundPlusKilogram() {
        Quantity<WeightUnit> result = QuantityWeightEquality.add(
                new Quantity<WeightUnit>(2.0, WeightUnit.POUND),
                new Quantity<WeightUnit>(1.0, WeightUnit.KILOGRAM));

        assertEquals(4.2046226218, result.getValue(), EPSILON, "Expected pounds + kilograms to add correctly");
    }

    @Test
    void testAddition_ExplicitTargetUnit_Gram() {
        Quantity<WeightUnit> result = QuantityWeightEquality.add(
                new Quantity<WeightUnit>(1.0, WeightUnit.KILOGRAM),
                new Quantity<WeightUnit>(1000.0, WeightUnit.GRAM),
                WeightUnit.GRAM);

        assertEquals(2000.0, result.getValue(), EPSILON, "Expected explicit target unit in grams");
        assertEquals(WeightUnit.GRAM, result.getUnit(), "Expected result unit to be grams");
    }

    @Test
    void testAddition_Commutativity() {
        Quantity<WeightUnit> first =
                new Quantity<WeightUnit>(1.0, WeightUnit.KILOGRAM);
        Quantity<WeightUnit> second =
                new Quantity<WeightUnit>(1000.0, WeightUnit.GRAM);

        Quantity<WeightUnit> resultFirst = QuantityWeightEquality.add(first, second);
        Quantity<WeightUnit> resultSecond = QuantityWeightEquality.add(second, first);

        double firstInKg = WeightUnit.KILOGRAM.convertToBaseUnit(
                resultFirst.convertTo(WeightUnit.KILOGRAM).getValue());
        double secondInKg = WeightUnit.KILOGRAM.convertToBaseUnit(
                resultSecond.convertTo(WeightUnit.KILOGRAM).getValue());

        assertEquals(firstInKg, secondInKg, EPSILON, "Expected addition to be commutative");
    }

    @Test
    void testAddition_WithZero() {
        Quantity<WeightUnit> result = QuantityWeightEquality.add(
                new Quantity<WeightUnit>(5.0, WeightUnit.KILOGRAM),
                new Quantity<WeightUnit>(0.0, WeightUnit.GRAM));

        assertEquals(5.0, result.getValue(), EPSILON, "Expected adding zero to preserve value");
    }

    @Test
    void testAddition_NegativeValues() {
        Quantity<WeightUnit> result = QuantityWeightEquality.add(
                new Quantity<WeightUnit>(5.0, WeightUnit.KILOGRAM),
                new Quantity<WeightUnit>(-2000.0, WeightUnit.GRAM));

        assertEquals(3.0, result.getValue(), EPSILON, "Expected negative values to add correctly");
    }

    @Test
    void testAddition_LargeValues() {
        Quantity<WeightUnit> result = QuantityWeightEquality.add(
                new Quantity<WeightUnit>(1e6, WeightUnit.KILOGRAM),
                new Quantity<WeightUnit>(1e6, WeightUnit.KILOGRAM));

        assertEquals(2e6, result.getValue(), EPSILON, "Expected large values to add correctly");
    }
}
