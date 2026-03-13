package com.imprakhartripathi.qmaserver.quantitymeasurement.service;

import com.imprakhartripathi.qmaserver.quantitymeasurement.model.QuantityDTO;
import com.imprakhartripathi.qmaserver.quantitymeasurement.model.QuantityMeasurementEntity;

public interface IQuantityMeasurementService {
    QuantityMeasurementEntity compare(QuantityDTO leftQuantity, QuantityDTO rightQuantity);

    QuantityMeasurementEntity convert(QuantityDTO sourceQuantity, String targetUnitName);

    QuantityMeasurementEntity add(QuantityDTO leftQuantity, QuantityDTO rightQuantity);

    QuantityMeasurementEntity add(QuantityDTO leftQuantity, QuantityDTO rightQuantity, String targetUnitName);

    QuantityMeasurementEntity subtract(QuantityDTO leftQuantity, QuantityDTO rightQuantity);

    QuantityMeasurementEntity subtract(QuantityDTO leftQuantity, QuantityDTO rightQuantity, String targetUnitName);

    QuantityMeasurementEntity divide(QuantityDTO leftQuantity, QuantityDTO rightQuantity);
}
