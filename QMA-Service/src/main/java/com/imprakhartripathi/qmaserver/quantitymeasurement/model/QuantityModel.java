package com.imprakhartripathi.qmaserver.quantitymeasurement.model;

import com.imprakhartripathi.qmaserver.equality.IMeasurable;

import java.util.Objects;

public class QuantityModel<U extends IMeasurable> {
    private final double value;
    private final U unit;

    public QuantityModel(double value, U unit) {
        this.value = value;
        this.unit = Objects.requireNonNull(unit, "Unit must not be null");
    }

    public double getValue() {
        return value;
    }

    public U getUnit() {
        return unit;
    }
}
