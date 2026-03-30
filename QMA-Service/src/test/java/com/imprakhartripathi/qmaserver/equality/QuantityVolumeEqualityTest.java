package com.imprakhartripathi.qmaserver.equality;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class QuantityVolumeEqualityTest {

    private static final double EPSILON = 1e-6;

    @Test
    void testEquality_LitreToLitre_SameValue() {
        Quantity<VolumeUnit> first = new Quantity<>(1.0, VolumeUnit.LITRE);
        Quantity<VolumeUnit> second = new Quantity<>(1.0, VolumeUnit.LITRE);

        assertTrue(first.equals(second), "Expected identical litre measurements to be equal");
    }

    @Test
    void testEquality_LitreToLitre_DifferentValue() {
        Quantity<VolumeUnit> first = new Quantity<>(1.0, VolumeUnit.LITRE);
        Quantity<VolumeUnit> second = new Quantity<>(2.0, VolumeUnit.LITRE);

        assertFalse(first.equals(second), "Expected different litre measurements to be unequal");
    }

    @Test
    void testEquality_LitreToMillilitre_EquivalentValue() {
        Quantity<VolumeUnit> litres = new Quantity<>(1.0, VolumeUnit.LITRE);
        Quantity<VolumeUnit> millilitres = new Quantity<>(1000.0, VolumeUnit.MILLILITRE);

        assertTrue(litres.equals(millilitres), "Expected 1.0 L to be equal to 1000.0 mL");
    }

    @Test
    void testEquality_MillilitreToLitre_EquivalentValue() {
        Quantity<VolumeUnit> millilitres = new Quantity<>(1000.0, VolumeUnit.MILLILITRE);
        Quantity<VolumeUnit> litres = new Quantity<>(1.0, VolumeUnit.LITRE);

        assertTrue(millilitres.equals(litres), "Expected 1000.0 mL to be equal to 1.0 L");
    }

    @Test
    void testEquality_LitreToGallon_EquivalentValue() {
        Quantity<VolumeUnit> litres = new Quantity<>(3.78541, VolumeUnit.LITRE);
        Quantity<VolumeUnit> gallons = new Quantity<>(1.0, VolumeUnit.GALLON);

        assertTrue(litres.equals(gallons), "Expected 3.78541 L to be equal to 1.0 gallon");
    }

    @Test
    void testEquality_GallonToLitre_EquivalentValue() {
        Quantity<VolumeUnit> gallons = new Quantity<>(1.0, VolumeUnit.GALLON);
        Quantity<VolumeUnit> litres = new Quantity<>(3.78541, VolumeUnit.LITRE);

        assertTrue(gallons.equals(litres), "Expected 1.0 gallon to be equal to 3.78541 L");
    }

    @Test
    void testEquality_VolumeVsLength_Incompatible() {
        Quantity<VolumeUnit> volume = new Quantity<>(1.0, VolumeUnit.LITRE);
        Quantity<LengthUnit> length = new Quantity<>(1.0, LengthUnit.FEET);

        assertFalse(volume.equals(length), "Expected volume not to be equal to length");
    }

    @Test
    void testEquality_VolumeVsWeight_Incompatible() {
        Quantity<VolumeUnit> volume = new Quantity<>(1.0, VolumeUnit.LITRE);
        Quantity<WeightUnit> weight = new Quantity<>(1.0, WeightUnit.KILOGRAM);

        assertFalse(volume.equals(weight), "Expected volume not to be equal to weight");
    }

    @Test
    void testEquality_NullComparison() {
        Quantity<VolumeUnit> volume = new Quantity<>(1.0, VolumeUnit.LITRE);

        assertFalse(volume.equals(null), "Expected volume not to be equal to null");
    }

    @Test
    void testEquality_SameReference() {
        Quantity<VolumeUnit> volume = new Quantity<>(1.0, VolumeUnit.LITRE);

        assertTrue(volume.equals(volume), "Expected volume to be equal to itself");
    }

    @Test
    void testEquality_NullUnit() {
        assertThrows(IllegalArgumentException.class,
                () -> new Quantity<>(1.0, null),
                "Expected null unit to be rejected");
    }

    @Test
    void testEquality_ZeroValue() {
        Quantity<VolumeUnit> litres = new Quantity<>(0.0, VolumeUnit.LITRE);
        Quantity<VolumeUnit> millilitres = new Quantity<>(0.0, VolumeUnit.MILLILITRE);

        assertTrue(litres.equals(millilitres), "Expected zero values to be equal across units");
    }

    @Test
    void testEquality_NegativeVolume() {
        Quantity<VolumeUnit> litres = new Quantity<>(-1.0, VolumeUnit.LITRE);
        Quantity<VolumeUnit> millilitres = new Quantity<>(-1000.0, VolumeUnit.MILLILITRE);

        assertTrue(litres.equals(millilitres), "Expected negative values to convert correctly");
    }

    @Test
    void testEquality_LargeVolumeValue() {
        Quantity<VolumeUnit> millilitres = new Quantity<>(1_000_000.0, VolumeUnit.MILLILITRE);
        Quantity<VolumeUnit> litres = new Quantity<>(1000.0, VolumeUnit.LITRE);

        assertTrue(millilitres.equals(litres), "Expected large values to convert correctly");
    }

    @Test
    void testEquality_SmallVolumeValue() {
        Quantity<VolumeUnit> litres = new Quantity<>(0.001, VolumeUnit.LITRE);
        Quantity<VolumeUnit> millilitres = new Quantity<>(1.0, VolumeUnit.MILLILITRE);

        assertTrue(litres.equals(millilitres), "Expected small values to convert correctly");
    }

    @Test
    void testConversion_LitreToMillilitre() {
        Quantity<VolumeUnit> result = new Quantity<>(1.0, VolumeUnit.LITRE)
                .convertTo(VolumeUnit.MILLILITRE);

        assertEquals(1000.0, result.getValue(), EPSILON, "Expected 1.0 L to convert to 1000.0 mL");
    }

    @Test
    void testConversion_MillilitreToLitre() {
        Quantity<VolumeUnit> result = new Quantity<>(1000.0, VolumeUnit.MILLILITRE)
                .convertTo(VolumeUnit.LITRE);

        assertEquals(1.0, result.getValue(), EPSILON, "Expected 1000.0 mL to convert to 1.0 L");
    }

    @Test
    void testConversion_GallonToLitre() {
        Quantity<VolumeUnit> result = new Quantity<>(1.0, VolumeUnit.GALLON)
                .convertTo(VolumeUnit.LITRE);

        assertEquals(3.78541, result.getValue(), EPSILON, "Expected 1.0 gallon to convert to litres");
    }

    @Test
    void testConversion_LitreToGallon() {
        Quantity<VolumeUnit> result = new Quantity<>(3.78541, VolumeUnit.LITRE)
                .convertTo(VolumeUnit.GALLON);

        assertEquals(1.0, result.getValue(), EPSILON, "Expected litres to convert to 1.0 gallon");
    }

    @Test
    void testConversion_MillilitreToGallon() {
        Quantity<VolumeUnit> result = new Quantity<>(1000.0, VolumeUnit.MILLILITRE)
                .convertTo(VolumeUnit.GALLON);

        assertEquals(0.264172, result.getValue(), 1e-5, "Expected mL to convert to gallons");
    }

    @Test
    void testConversion_SameUnit() {
        Quantity<VolumeUnit> result = new Quantity<>(5.0, VolumeUnit.LITRE)
                .convertTo(VolumeUnit.LITRE);

        assertEquals(5.0, result.getValue(), EPSILON, "Expected same-unit conversion to preserve value");
    }

    @Test
    void testConversion_ZeroValue() {
        Quantity<VolumeUnit> result = new Quantity<>(0.0, VolumeUnit.LITRE)
                .convertTo(VolumeUnit.MILLILITRE);

        assertEquals(0.0, result.getValue(), EPSILON, "Expected zero conversion to be zero");
    }

    @Test
    void testConversion_NegativeValue() {
        Quantity<VolumeUnit> result = new Quantity<>(-1.0, VolumeUnit.LITRE)
                .convertTo(VolumeUnit.MILLILITRE);

        assertEquals(-1000.0, result.getValue(), EPSILON, "Expected negative conversion to preserve sign");
    }

    @Test
    void testConversion_RoundTrip() {
        Quantity<VolumeUnit> result = new Quantity<>(1.5, VolumeUnit.LITRE)
                .convertTo(VolumeUnit.MILLILITRE)
                .convertTo(VolumeUnit.LITRE);

        assertEquals(1.5, result.getValue(), EPSILON, "Expected round-trip conversion to preserve value");
    }

    @Test
    void testAddition_SameUnit_LitrePlusLitre() {
        Quantity<VolumeUnit> result = QuantityVolumeEquality.add(
                new Quantity<>(1.0, VolumeUnit.LITRE),
                new Quantity<>(2.0, VolumeUnit.LITRE));

        assertEquals(3.0, result.getValue(), EPSILON, "Expected 1.0 L + 2.0 L = 3.0 L");
    }

    @Test
    void testAddition_SameUnit_MillilitrePlusMillilitre() {
        Quantity<VolumeUnit> result = QuantityVolumeEquality.add(
                new Quantity<>(500.0, VolumeUnit.MILLILITRE),
                new Quantity<>(500.0, VolumeUnit.MILLILITRE));

        assertEquals(1000.0, result.getValue(), EPSILON, "Expected 500.0 mL + 500.0 mL = 1000.0 mL");
    }

    @Test
    void testAddition_CrossUnit_LitrePlusMillilitre() {
        Quantity<VolumeUnit> result = QuantityVolumeEquality.add(
                new Quantity<>(1.0, VolumeUnit.LITRE),
                new Quantity<>(1000.0, VolumeUnit.MILLILITRE));

        assertEquals(2.0, result.getValue(), EPSILON, "Expected 1.0 L + 1000.0 mL = 2.0 L");
    }

    @Test
    void testAddition_CrossUnit_MillilitrePlusLitre() {
        Quantity<VolumeUnit> result = QuantityVolumeEquality.add(
                new Quantity<>(1000.0, VolumeUnit.MILLILITRE),
                new Quantity<>(1.0, VolumeUnit.LITRE));

        assertEquals(2000.0, result.getValue(), EPSILON, "Expected 1000.0 mL + 1.0 L = 2000.0 mL");
    }

    @Test
    void testAddition_CrossUnit_GallonPlusLitre() {
        Quantity<VolumeUnit> result = QuantityVolumeEquality.add(
                new Quantity<>(1.0, VolumeUnit.GALLON),
                new Quantity<>(3.78541, VolumeUnit.LITRE));

        assertEquals(2.0, result.getValue(), EPSILON, "Expected 1.0 gallon + 3.78541 L = 2.0 gallons");
    }

    @Test
    void testAddition_ExplicitTargetUnit_Litre() {
        Quantity<VolumeUnit> result = QuantityVolumeEquality.add(
                new Quantity<>(1.0, VolumeUnit.LITRE),
                new Quantity<>(1000.0, VolumeUnit.MILLILITRE),
                VolumeUnit.LITRE);

        assertEquals(2.0, result.getValue(), EPSILON, "Expected explicit target unit in litres");
    }

    @Test
    void testAddition_ExplicitTargetUnit_Millilitre() {
        Quantity<VolumeUnit> result = QuantityVolumeEquality.add(
                new Quantity<>(1.0, VolumeUnit.LITRE),
                new Quantity<>(1000.0, VolumeUnit.MILLILITRE),
                VolumeUnit.MILLILITRE);

        assertEquals(2000.0, result.getValue(), EPSILON, "Expected explicit target unit in millilitres");
    }

    @Test
    void testAddition_ExplicitTargetUnit_Gallon() {
        Quantity<VolumeUnit> result = QuantityVolumeEquality.add(
                new Quantity<>(3.78541, VolumeUnit.LITRE),
                new Quantity<>(3.78541, VolumeUnit.LITRE),
                VolumeUnit.GALLON);

        assertEquals(2.0, result.getValue(), EPSILON, "Expected explicit target unit in gallons");
    }

    @Test
    void testAddition_WithZero() {
        Quantity<VolumeUnit> result = QuantityVolumeEquality.add(
                new Quantity<>(5.0, VolumeUnit.LITRE),
                new Quantity<>(0.0, VolumeUnit.MILLILITRE));

        assertEquals(5.0, result.getValue(), EPSILON, "Expected adding zero to preserve value");
    }

    @Test
    void testAddition_NegativeValues() {
        Quantity<VolumeUnit> result = QuantityVolumeEquality.add(
                new Quantity<>(5.0, VolumeUnit.LITRE),
                new Quantity<>(-2000.0, VolumeUnit.MILLILITRE));

        assertEquals(3.0, result.getValue(), EPSILON, "Expected negative values to add correctly");
    }

    @Test
    void testAddition_LargeValues() {
        Quantity<VolumeUnit> result = QuantityVolumeEquality.add(
                new Quantity<>(1e6, VolumeUnit.LITRE),
                new Quantity<>(1e6, VolumeUnit.LITRE));

        assertEquals(2e6, result.getValue(), EPSILON, "Expected large values to add correctly");
    }

    @Test
    void testAddition_SmallValues() {
        Quantity<VolumeUnit> result = QuantityVolumeEquality.add(
                new Quantity<>(0.001, VolumeUnit.LITRE),
                new Quantity<>(0.002, VolumeUnit.LITRE));

        assertEquals(0.003, result.getValue(), EPSILON, "Expected small values to add correctly");
    }

    @Test
    void testVolumeUnitEnum_LitreConstant() {
        assertEquals(1.0, VolumeUnit.LITRE.getConversionFactor(), EPSILON, "Expected litre factor to be 1.0");
    }

    @Test
    void testVolumeUnitEnum_MillilitreConstant() {
        assertEquals(0.001, VolumeUnit.MILLILITRE.getConversionFactor(), EPSILON,
                "Expected millilitre factor to be 0.001");
    }

    @Test
    void testVolumeUnitEnum_GallonConstant() {
        assertEquals(3.78541, VolumeUnit.GALLON.getConversionFactor(), EPSILON,
                "Expected gallon factor to be 3.78541");
    }

    @Test
    void testConvertToBaseUnit_LitreToLitre() {
        assertEquals(5.0, VolumeUnit.LITRE.convertToBaseUnit(5.0), EPSILON, "Expected 5.0 L to stay 5.0 L");
    }

    @Test
    void testConvertToBaseUnit_MillilitreToLitre() {
        assertEquals(1.0, VolumeUnit.MILLILITRE.convertToBaseUnit(1000.0), EPSILON,
                "Expected 1000.0 mL to be 1.0 L");
    }

    @Test
    void testConvertToBaseUnit_GallonToLitre() {
        assertEquals(3.78541, VolumeUnit.GALLON.convertToBaseUnit(1.0), EPSILON,
                "Expected 1.0 gallon to be 3.78541 L");
    }

    @Test
    void testConvertFromBaseUnit_LitreToLitre() {
        assertEquals(2.0, VolumeUnit.LITRE.convertFromBaseUnit(2.0), EPSILON,
                "Expected 2.0 L to stay 2.0 L");
    }

    @Test
    void testConvertFromBaseUnit_LitreToMillilitre() {
        assertEquals(1000.0, VolumeUnit.MILLILITRE.convertFromBaseUnit(1.0), EPSILON,
                "Expected 1.0 L to be 1000.0 mL");
    }

    @Test
    void testConvertFromBaseUnit_LitreToGallon() {
        assertEquals(1.0, VolumeUnit.GALLON.convertFromBaseUnit(3.78541), EPSILON,
                "Expected 3.78541 L to be 1.0 gallon");
    }
}
