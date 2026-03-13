package com.imprakhartripathi.qmaserver.quantitymeasurement.repository;

import com.imprakhartripathi.qmaserver.quantitymeasurement.util.ApplicationConfig;

public final class QuantityMeasurementRepositoryFactory {
    private QuantityMeasurementRepositoryFactory() {
    }

    public static IQuantityMeasurementRepository create(ApplicationConfig config) {
        if (config.useDatabaseRepository()) {
            return new QuantityMeasurementDatabaseRepository(config);
        }
        return QuantityMeasurementCacheRepository.getInstance();
    }
}
