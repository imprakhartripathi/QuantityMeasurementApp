package com.imprakhartripathi.qmaserver.equality;

/**
 * Supported volume units with conversion factors to litres.
 */
public enum VolumeUnit implements IMeasurable {
    LITRE(1.0),
    MILLILITRE(0.001),
    GALLON(3.78541);

    private final double toLitreFactor;

    VolumeUnit(double toLitreFactor) {
        this.toLitreFactor = toLitreFactor;
    }

    @Override
    public double getConversionFactor() {
        return toLitreFactor;
    }

    @Override
    public double convertToBaseUnit(double value) {
        return value * toLitreFactor;
    }

    @Override
    public double convertFromBaseUnit(double baseValue) {
        return baseValue / toLitreFactor;
    }

    @Override
    public String getUnitName() {
        return name();
    }
}
