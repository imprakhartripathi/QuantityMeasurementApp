package com.imprakhartripathi.qmaserver.quantitymeasurement.service;

import com.imprakhartripathi.qmaserver.quantitymeasurement.model.OperationType;
import com.imprakhartripathi.qmaserver.quantitymeasurement.model.QuantityDTO;
import com.imprakhartripathi.qmaserver.quantitymeasurement.model.QuantityMeasurementDTO;

import java.util.List;

public interface IQuantityMeasurementService {
    QuantityMeasurementDTO compare(QuantityDTO leftQuantity, QuantityDTO rightQuantity);

    QuantityMeasurementDTO convert(QuantityDTO sourceQuantity, String targetUnitName);

    QuantityMeasurementDTO add(QuantityDTO leftQuantity, QuantityDTO rightQuantity);

    QuantityMeasurementDTO add(QuantityDTO leftQuantity, QuantityDTO rightQuantity, String targetUnitName);

    QuantityMeasurementDTO subtract(QuantityDTO leftQuantity, QuantityDTO rightQuantity);

    QuantityMeasurementDTO subtract(QuantityDTO leftQuantity, QuantityDTO rightQuantity, String targetUnitName);

    QuantityMeasurementDTO divide(QuantityDTO leftQuantity, QuantityDTO rightQuantity);

    List<QuantityMeasurementDTO> getOperationHistory(OperationType operationType);

    List<QuantityMeasurementDTO> getMeasurementHistory(String measurementType);

    long getOperationCount(OperationType operationType);

    List<QuantityMeasurementDTO> getErroredHistory();
}
