package com.imprakhartripathi.qmaserver.equality;

import java.util.function.Function;

public enum TemperatureUnit implements IMeasurable {
    CELSIUS(celsius -> celsius, celsius -> celsius),
    FAHRENHEIT(fahrenheit -> (fahrenheit - 32.0) * 5.0 / 9.0,
            celsius -> (celsius * 9.0 / 5.0) + 32.0),
    KELVIN(kelvin -> kelvin - 273.15, celsius -> celsius + 273.15);

    private final Function<Double, Double> toCelsius;
    private final Function<Double, Double> fromCelsius;
    private final SupportsArithmetic supportsArithmetic = () -> false;

    TemperatureUnit(Function<Double, Double> toCelsius, Function<Double, Double> fromCelsius) {
        this.toCelsius = toCelsius;
        this.fromCelsius = fromCelsius;
    }

    @Override
    public double getConversionFactor() {
        return 1.0;
    }

    @Override
    public double convertToBaseUnit(double value) {
        return toCelsius.apply(value);
    }

    @Override
    public double convertFromBaseUnit(double baseValue) {
        return fromCelsius.apply(baseValue);
    }

    @Override
    public String getUnitName() {
        return name();
    }

    @Override
    public boolean supportsArithmetic() {
        return supportsArithmetic.isSupported();
    }

    @Override
    public void validateOperationSupport(String operation) {
        throw new UnsupportedOperationException("Temperature does not support " + operation.toLowerCase());
    }
}
