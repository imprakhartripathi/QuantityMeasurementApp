package com.imprakhartripathi.qmaserver.quantitymeasurement.model;

import com.imprakhartripathi.qmaserver.equality.IMeasurable;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public class QuantityDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final double value;
    private final String unitName;
    private final String measurementType;

    public QuantityDTO(double value, String unitName, String measurementType) {
        this.value = value;
        this.unitName = Objects.requireNonNull(unitName, "Unit name must not be null");
        this.measurementType = Objects.requireNonNull(measurementType, "Measurement type must not be null");
    }

    public double getValue() {
        return value;
    }

    public String getUnitName() {
        return unitName;
    }

    public String getMeasurementType() {
        return measurementType;
    }

    public static QuantityDTO from(double value, IMeasurable unit) {
        return new QuantityDTO(value, unit.getUnitName(), unit.getMeasurementType());
    }

    @Override
    public String toString() {
        return "QuantityDTO{" +
                "value=" + value +
                ", unitName='" + unitName + '\'' +
                ", measurementType='" + measurementType + '\'' +
                '}';
    }
}
