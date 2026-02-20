package com.imprakhartripathi.qmaserver.equality;

/**
 * Supported weight units with conversion factors to kilograms.
 */
public enum WeightUnit {
    KILOGRAM(1.0),
    GRAM(0.001),
    POUND(0.45359237);

    private final double toKilogramFactor;

    WeightUnit(double toKilogramFactor) {
        this.toKilogramFactor = toKilogramFactor;
    }

    public double convertToBaseUnit(double value) {
        return value * toKilogramFactor;
    }

    public double convertFromBaseUnit(double baseValue) {
        return baseValue / toKilogramFactor;
    }
}
