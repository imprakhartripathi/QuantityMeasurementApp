package com.imprakhartripathi.qmaserver.equality;

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

    default boolean supportsArithmetic() {
        return DEFAULT_SUPPORTS_ARITHMETIC.isSupported();
    }

    default void validateOperationSupport(String operation) {
        // Default: all operations are supported
    }
}
