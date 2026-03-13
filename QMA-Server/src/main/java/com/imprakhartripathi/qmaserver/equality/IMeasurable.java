package com.imprakhartripathi.qmaserver.equality;

import java.util.Locale;

public interface IMeasurable {
    @FunctionalInterface
    interface SupportsArithmetic {
        boolean isSupported();
    }

    SupportsArithmetic DEFAULT_SUPPORTS_ARITHMETIC = () -> true;

    double getConversionFactor();

    double convertToBaseUnit(double value);

    double convertFromBaseUnit(double baseValue);

    String getUnitName();

    String getMeasurementType();

    default boolean supportsArithmetic() {
        return DEFAULT_SUPPORTS_ARITHMETIC.isSupported();
    }

    default void validateOperationSupport(String operation) {
        // Default: all operations are supported
    }

    static IMeasurable resolveUnit(String measurementType, String unitName) {
        if (measurementType == null || unitName == null) {
            throw new IllegalArgumentException("Measurement type and unit name must not be null");
        }

        String normalizedType = measurementType.trim().toUpperCase(Locale.ROOT);
        String normalizedUnit = unitName.trim().toUpperCase(Locale.ROOT);

        return switch (normalizedType) {
            case "LENGTH" -> LengthUnit.valueOf(normalizedUnit);
            case "WEIGHT" -> WeightUnit.valueOf(normalizedUnit);
            case "VOLUME" -> VolumeUnit.valueOf(normalizedUnit);
            case "TEMPERATURE" -> TemperatureUnit.valueOf(normalizedUnit);
            default -> throw new IllegalArgumentException("Unsupported measurement type: " + measurementType);
        };
    }
}
