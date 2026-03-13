package com.imprakhartripathi.qmaserver.quantitymeasurement.controller;

import com.imprakhartripathi.qmaserver.quantitymeasurement.model.QuantityDTO;
import com.imprakhartripathi.qmaserver.quantitymeasurement.model.QuantityMeasurementEntity;
import com.imprakhartripathi.qmaserver.quantitymeasurement.service.IQuantityMeasurementService;

import java.util.Objects;

public class QuantityMeasurementController {
    private final IQuantityMeasurementService quantityMeasurementService;

    public QuantityMeasurementController(IQuantityMeasurementService quantityMeasurementService) {
        this.quantityMeasurementService = Objects.requireNonNull(quantityMeasurementService,
                "Quantity measurement service must not be null");
    }

    public QuantityMeasurementEntity performComparison(QuantityDTO leftQuantity, QuantityDTO rightQuantity) {
        return quantityMeasurementService.compare(leftQuantity, rightQuantity);
    }

    public QuantityMeasurementEntity performConversion(QuantityDTO sourceQuantity, String targetUnitName) {
        return quantityMeasurementService.convert(sourceQuantity, targetUnitName);
    }

    public QuantityMeasurementEntity performAddition(QuantityDTO leftQuantity, QuantityDTO rightQuantity) {
        return quantityMeasurementService.add(leftQuantity, rightQuantity);
    }

    public QuantityMeasurementEntity performAddition(QuantityDTO leftQuantity, QuantityDTO rightQuantity,
                                                     String targetUnitName) {
        return quantityMeasurementService.add(leftQuantity, rightQuantity, targetUnitName);
    }

    public QuantityMeasurementEntity performSubtraction(QuantityDTO leftQuantity, QuantityDTO rightQuantity) {
        return quantityMeasurementService.subtract(leftQuantity, rightQuantity);
    }

    public QuantityMeasurementEntity performSubtraction(QuantityDTO leftQuantity, QuantityDTO rightQuantity,
                                                        String targetUnitName) {
        return quantityMeasurementService.subtract(leftQuantity, rightQuantity, targetUnitName);
    }

    public QuantityMeasurementEntity performDivision(QuantityDTO leftQuantity, QuantityDTO rightQuantity) {
        return quantityMeasurementService.divide(leftQuantity, rightQuantity);
    }

    public String displayResult(QuantityMeasurementEntity entity) {
        if (entity.hasError()) {
            return "ERROR: " + entity.getErrorMessage();
        }
        if (entity.getResultQuantity() != null) {
            QuantityDTO result = entity.getResultQuantity();
            return entity.getOperationType() + ": " + result.getValue() + " " + result.getUnitName();
        }
        if (entity.getComparisonResult() != null) {
            return entity.getOperationType() + ": " + entity.getComparisonResult();
        }
        return entity.getOperationType() + ": " + entity.getScalarResult();
    }
}
