package com.imprakhartripathi.qmaserver.quantitymeasurement.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.imprakhartripathi.qmaserver.equality.IMeasurable;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class QuantityDTO {
    @NotNull(message = "Value must be provided")
    private Double value;

    @NotBlank(message = "Unit must be provided")
    @JsonAlias("unit")
    private String unitName;

    @NotBlank(message = "Measurement type must be provided")
    private String measurementType;

    public QuantityDTO() {
    }

    public QuantityDTO(double value, String unitName, String measurementType) {
        this.value = value;
        this.unitName = unitName;
        this.measurementType = measurementType;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getUnitName() {
        return unitName;
    }

    public String getUnit() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public void setUnit(String unitName) {
        this.unitName = unitName;
    }

    public String getMeasurementType() {
        return measurementType;
    }

    public void setMeasurementType(String measurementType) {
        this.measurementType = measurementType;
    }

    @AssertTrue(message = "Unit must be valid for the specified measurement type")
    public boolean isValidUnitForMeasurementType() {
        if (measurementType == null || measurementType.isBlank() || unitName == null || unitName.isBlank()) {
            return true;
        }
        try {
            IMeasurable.resolveUnit(measurementType, unitName);
            return true;
        } catch (IllegalArgumentException exception) {
            return false;
        }
    }

    public static QuantityDTO from(double value, IMeasurable unit) {
        return new QuantityDTO(value, unit.getUnitName(), unit.getMeasurementType());
    }
}
