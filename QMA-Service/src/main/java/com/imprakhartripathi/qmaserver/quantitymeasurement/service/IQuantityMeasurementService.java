package com.imprakhartripathi.qmaserver.quantitymeasurement.service;

import com.imprakhartripathi.qmaserver.quantitymeasurement.model.OperationType;
import com.imprakhartripathi.qmaserver.quantitymeasurement.model.QuantityDTO;
import com.imprakhartripathi.qmaserver.quantitymeasurement.model.QuantityMeasurementDTO;

import java.util.List;

public interface IQuantityMeasurementService {
    default QuantityMeasurementDTO compare(QuantityDTO leftQuantity, QuantityDTO rightQuantity) {
        return compare(leftQuantity, rightQuantity, null);
    }

    QuantityMeasurementDTO compare(QuantityDTO leftQuantity, QuantityDTO rightQuantity, String userEmail);

    default QuantityMeasurementDTO convert(QuantityDTO sourceQuantity, String targetUnitName) {
        return convert(sourceQuantity, targetUnitName, null);
    }

    QuantityMeasurementDTO convert(QuantityDTO sourceQuantity, String targetUnitName, String userEmail);

    default QuantityMeasurementDTO add(QuantityDTO leftQuantity, QuantityDTO rightQuantity) {
        return add(leftQuantity, rightQuantity, leftQuantity != null ? leftQuantity.getUnitName() : null, null);
    }

    default QuantityMeasurementDTO add(QuantityDTO leftQuantity, QuantityDTO rightQuantity, String targetUnitName) {
        return add(leftQuantity, rightQuantity, targetUnitName, null);
    }

    QuantityMeasurementDTO add(QuantityDTO leftQuantity, QuantityDTO rightQuantity, String targetUnitName, String userEmail);

    default QuantityMeasurementDTO subtract(QuantityDTO leftQuantity, QuantityDTO rightQuantity) {
        return subtract(leftQuantity, rightQuantity, leftQuantity != null ? leftQuantity.getUnitName() : null, null);
    }

    default QuantityMeasurementDTO subtract(QuantityDTO leftQuantity, QuantityDTO rightQuantity, String targetUnitName) {
        return subtract(leftQuantity, rightQuantity, targetUnitName, null);
    }

    QuantityMeasurementDTO subtract(QuantityDTO leftQuantity, QuantityDTO rightQuantity, String targetUnitName, String userEmail);

    default QuantityMeasurementDTO divide(QuantityDTO leftQuantity, QuantityDTO rightQuantity) {
        return divide(leftQuantity, rightQuantity, null);
    }

    QuantityMeasurementDTO divide(QuantityDTO leftQuantity, QuantityDTO rightQuantity, String userEmail);

    List<QuantityMeasurementDTO> getOperationHistory(OperationType operationType);

    List<QuantityMeasurementDTO> getMeasurementHistory(String measurementType);

    long getOperationCount(OperationType operationType);

    List<QuantityMeasurementDTO> getErroredHistory();

    List<QuantityMeasurementDTO> getHistoryForUser(String userEmail);
}
