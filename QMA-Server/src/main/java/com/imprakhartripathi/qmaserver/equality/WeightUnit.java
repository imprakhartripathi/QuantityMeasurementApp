package com.imprakhartripathi.qmaserver.equality;

/**
 * Supported weight units with conversion factors to kilograms.
 */
public enum WeightUnit implements IMeasurable {
    KILOGRAM(1.0),
    GRAM(0.001),
    POUND(0.45359237),
    OUNCE(0.028349523125);

    private final double toKilogramFactor;

    WeightUnit(double toKilogramFactor) {
        this.toKilogramFactor = toKilogramFactor;
    }

    @Override
    public double getConversionFactor() {
        return toKilogramFactor;
    }

    @Override
    public double convertToBaseUnit(double value) {
        return value * toKilogramFactor;
    }

    @Override
    public double convertFromBaseUnit(double baseValue) {
        return baseValue / toKilogramFactor;
    }

    @Override
    public String getUnitName() {
        return name();
    }

    @Override
    public String getMeasurementType() {
        return "WEIGHT";
    }
}
