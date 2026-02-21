package com.imprakhartripathi.qmaserver.equality;

/**
 * Supported length units with conversion factors to feet.
 */
public enum LengthUnit implements IMeasurable {
    FEET(1.0),
    INCH(1.0 / 12.0),
    YARDS(3.0),
    CENTIMETERS(1.0 / 30.48);

    private final double toFeetFactor;

    LengthUnit(double toFeetFactor) {
        this.toFeetFactor = toFeetFactor;
    }

    @Override
    public double getConversionFactor() {
        return toFeetFactor;
    }

    @Override
    public double convertToBaseUnit(double value) {
        return value * toFeetFactor;
    }

    @Override
    public double convertFromBaseUnit(double baseValue) {
        return baseValue / toFeetFactor;
    }

    @Override
    public String getUnitName() {
        return name();
    }
}
