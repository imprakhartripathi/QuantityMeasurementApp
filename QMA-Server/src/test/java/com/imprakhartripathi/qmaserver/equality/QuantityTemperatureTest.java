package com.imprakhartripathi.qmaserver.equality;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class QuantityTemperatureTest {

    private static final double EPSILON = 1e-6;

    @Test
    void testTemperatureEquality_CelsiusToFahrenheit() {
        Quantity<TemperatureUnit> celsius = new Quantity<>(0.0, TemperatureUnit.CELSIUS);
        Quantity<TemperatureUnit> fahrenheit = new Quantity<>(32.0, TemperatureUnit.FAHRENHEIT);

        assertTrue(celsius.equals(fahrenheit), "Expected 0°C to equal 32°F");
    }

    @Test
    void testTemperatureEquality_CelsiusToKelvin() {
        Quantity<TemperatureUnit> celsius = new Quantity<>(0.0, TemperatureUnit.CELSIUS);
        Quantity<TemperatureUnit> kelvin = new Quantity<>(273.15, TemperatureUnit.KELVIN);

        assertTrue(celsius.equals(kelvin), "Expected 0°C to equal 273.15 K");
    }

    @Test
    void testTemperatureConversion_CelsiusToFahrenheit() {
        Quantity<TemperatureUnit> result = new Quantity<>(100.0, TemperatureUnit.CELSIUS)
                .convertTo(TemperatureUnit.FAHRENHEIT);

        assertEquals(212.0, result.getValue(), EPSILON, "Expected 100°C to be 212°F");
    }

    @Test
    void testTemperatureConversion_FahrenheitToCelsius() {
        Quantity<TemperatureUnit> result = new Quantity<>(32.0, TemperatureUnit.FAHRENHEIT)
                .convertTo(TemperatureUnit.CELSIUS);

        assertEquals(0.0, result.getValue(), EPSILON, "Expected 32°F to be 0°C");
    }

    @Test
    void testTemperatureUnsupported_Add() {
        assertThrows(UnsupportedOperationException.class,
                () -> new Quantity<>(100.0, TemperatureUnit.CELSIUS)
                        .add(new Quantity<>(50.0, TemperatureUnit.CELSIUS)));
    }

    @Test
    void testTemperatureUnsupported_Subtract() {
        assertThrows(UnsupportedOperationException.class,
                () -> new Quantity<>(100.0, TemperatureUnit.CELSIUS)
                        .subtract(new Quantity<>(50.0, TemperatureUnit.CELSIUS)));
    }

    @Test
    void testTemperatureUnsupported_Divide() {
        assertThrows(UnsupportedOperationException.class,
                () -> new Quantity<>(100.0, TemperatureUnit.CELSIUS)
                        .divide(new Quantity<>(50.0, TemperatureUnit.CELSIUS)));
    }

    @Test
    void testTemperatureVsLength_Incompatible() {
        Quantity<TemperatureUnit> temperature = new Quantity<>(25.0, TemperatureUnit.CELSIUS);
        Quantity<LengthUnit> length = new Quantity<>(25.0, LengthUnit.FEET);

        assertFalse(temperature.equals(length), "Expected temperature not to equal length");
    }
}
