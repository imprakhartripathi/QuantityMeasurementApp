package com.imprakhartripathi.qmaserver.quantitymeasurement.integration;

import com.imprakhartripathi.qmaserver.quantitymeasurement.controller.QuantityMeasurementController;
import com.imprakhartripathi.qmaserver.quantitymeasurement.model.QuantityDTO;
import com.imprakhartripathi.qmaserver.quantitymeasurement.repository.IQuantityMeasurementRepository;
import com.imprakhartripathi.qmaserver.quantitymeasurement.repository.QuantityMeasurementDatabaseRepository;
import com.imprakhartripathi.qmaserver.quantitymeasurement.service.QuantityMeasurementServiceImpl;
import com.imprakhartripathi.qmaserver.quantitymeasurement.support.PostgreSqlTestSupport;
import com.imprakhartripathi.qmaserver.quantitymeasurement.util.ApplicationConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class QuantityMeasurementIntegrationTest {
    private IQuantityMeasurementRepository repository;
    private QuantityMeasurementController controller;

    @BeforeEach
    void setUp() {
        PostgreSqlTestSupport.ensureDatabase("integration_test");
        System.setProperty("quantity.measurement.database.url", PostgreSqlTestSupport.jdbcUrl("integration_test"));
        System.setProperty("quantity.measurement.database.username", "postgres");
        System.setProperty("quantity.measurement.database.password", "");
        repository = new QuantityMeasurementDatabaseRepository(ApplicationConfig.load());
        repository.deleteAllMeasurements();
        controller = new QuantityMeasurementController(new QuantityMeasurementServiceImpl(repository));
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
    void testIntegrationEndToEndDatabasePersistence() {
        controller.performAddition(
                new QuantityDTO(1.0, "FEET", "LENGTH"),
                new QuantityDTO(12.0, "INCH", "LENGTH"));
        controller.performConversion(
                new QuantityDTO(100.0, "CELSIUS", "TEMPERATURE"),
                "FAHRENHEIT");

        assertEquals(2, repository.getTotalCount());
        assertEquals(1, repository.getMeasurementsByOperation("ADD").size());
        assertEquals(1, repository.getMeasurementsByMeasurementType("TEMPERATURE").size());
    }
}
