package com.imprakhartripathi.qmaserver.quantitymeasurement.repository;

import com.imprakhartripathi.qmaserver.quantitymeasurement.model.QuantityMeasurementEntity;

import java.util.List;

public interface IQuantityMeasurementRepository {
    QuantityMeasurementEntity save(QuantityMeasurementEntity entity);

    List<QuantityMeasurementEntity> getAllMeasurements();

    void clear();
}
