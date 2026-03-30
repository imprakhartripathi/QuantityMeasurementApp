package com.imprakhartripathi.qmaserver.equality;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class QuantityArithmeticTest {

    private static final double EPSILON = 1e-6;

    @Test
    void testSubtraction_CrossUnit_FeetMinusInches() {
        Quantity<LengthUnit> result = new Quantity<>(10.0, LengthUnit.FEET)
                .subtract(new Quantity<>(6.0, LengthUnit.INCH));

        assertEquals(9.5, result.getValue(), EPSILON, "Expected 10 ft - 6 in = 9.5 ft");
        assertEquals(LengthUnit.FEET, result.getUnit(), "Expected result unit to be feet");
    }

    @Test
    void testSubtraction_ExplicitTargetUnit_Inches() {
        Quantity<LengthUnit> result = new Quantity<>(10.0, LengthUnit.FEET)
                .subtract(new Quantity<>(6.0, LengthUnit.INCH), LengthUnit.INCH);

        assertEquals(114.0, result.getValue(), EPSILON, "Expected 10 ft - 6 in = 114 in");
        assertEquals(LengthUnit.INCH, result.getUnit(), "Expected result unit to be inches");
    }

    @Test
    void testSubtraction_Weight() {
        Quantity<WeightUnit> result = new Quantity<>(10.0, WeightUnit.KILOGRAM)
                .subtract(new Quantity<>(5000.0, WeightUnit.GRAM));

        assertEquals(5.0, result.getValue(), EPSILON, "Expected 10 kg - 5000 g = 5 kg");
    }

    @Test
    void testSubtraction_Volume() {
        Quantity<VolumeUnit> result = new Quantity<>(5.0, VolumeUnit.LITRE)
                .subtract(new Quantity<>(500.0, VolumeUnit.MILLILITRE));

        assertEquals(4.5, result.getValue(), EPSILON, "Expected 5 L - 500 mL = 4.5 L");
    }

    @Test
    @SuppressWarnings({"rawtypes", "unchecked"})
    void testSubtraction_CrossCategory_Throws() {
        assertThrows(IllegalArgumentException.class,
                () -> new Quantity<>(1.0, LengthUnit.FEET)
                        .subtract((Quantity) new Quantity<>(1.0, WeightUnit.KILOGRAM)));
    }

    @Test
    void testDivision_CrossUnit() {
        double result = new Quantity<>(24.0, LengthUnit.INCH)
                .divide(new Quantity<>(2.0, LengthUnit.FEET));

        assertEquals(1.0, result, EPSILON, "Expected 24 in / 2 ft = 1.0");
    }

    @Test
    void testDivision_ByZero_Throws() {
        assertThrows(ArithmeticException.class,
                () -> new Quantity<>(10.0, LengthUnit.FEET)
                        .divide(new Quantity<>(0.0, LengthUnit.FEET)));
    }
}
