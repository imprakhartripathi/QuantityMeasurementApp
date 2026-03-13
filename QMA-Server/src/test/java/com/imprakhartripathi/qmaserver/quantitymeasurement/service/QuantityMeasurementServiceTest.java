package com.imprakhartripathi.qmaserver.quantitymeasurement.service;

import com.imprakhartripathi.qmaserver.quantitymeasurement.model.QuantityDTO;
import com.imprakhartripathi.qmaserver.quantitymeasurement.model.QuantityMeasurementEntity;
import com.imprakhartripathi.qmaserver.quantitymeasurement.repository.IQuantityMeasurementRepository;
import com.imprakhartripathi.qmaserver.quantitymeasurement.repository.QuantityMeasurementDatabaseRepository;
import com.imprakhartripathi.qmaserver.quantitymeasurement.support.PostgreSqlTestSupport;
import com.imprakhartripathi.qmaserver.quantitymeasurement.util.ApplicationConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class QuantityMeasurementServiceTest {
    private IQuantityMeasurementRepository repository;
    private QuantityMeasurementServiceImpl service;

    @BeforeEach
    void setUp() {
        PostgreSqlTestSupport.ensureDatabase("service_test");
        System.setProperty("quantity.measurement.database.url", PostgreSqlTestSupport.jdbcUrl("service_test"));
        System.setProperty("quantity.measurement.database.username", "postgres");
        System.setProperty("quantity.measurement.database.password", "");
        repository = new QuantityMeasurementDatabaseRepository(ApplicationConfig.load());
        repository.deleteAllMeasurements();
        service = new QuantityMeasurementServiceImpl(repository);
    }

    @AfterEach
    void tearDown() {
        if (repository != null) {
            repository.releaseResources();
        }
        System.clearProperty("quantity.measurement.database.url");
        System.clearProperty("quantity.measurement.database.username");
        System.clearProperty("quantity.measurement.database.password");
    }

    @Test
    void testServiceWithDatabaseRepositoryIntegration() {
        QuantityMeasurementEntity entity = service.compare(
                new QuantityDTO(1.0, "FEET", "LENGTH"),
                new QuantityDTO(12.0, "INCH", "LENGTH"));

        assertFalse(entity.hasError());
        assertTrue(entity.getComparisonResult());
        assertEquals(1, repository.getTotalCount());
    }

    @Test
    void testServiceErrorStillPersistsToRepository() {
        QuantityMeasurementEntity entity = service.add(
                new QuantityDTO(100.0, "CELSIUS", "TEMPERATURE"),
                new QuantityDTO(50.0, "CELSIUS", "TEMPERATURE"));

        assertTrue(entity.hasError());
        assertEquals(1, repository.getTotalCount());
    }
}
