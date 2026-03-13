package com.imprakhartripathi.qmaserver.quantitymeasurement.repository;

import com.imprakhartripathi.qmaserver.quantitymeasurement.model.QuantityDTO;
import com.imprakhartripathi.qmaserver.quantitymeasurement.model.QuantityMeasurementEntity;
import com.imprakhartripathi.qmaserver.quantitymeasurement.support.PostgreSqlTestSupport;
import com.imprakhartripathi.qmaserver.quantitymeasurement.util.ApplicationConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class QuantityMeasurementDatabaseRepositoryTest {
    private QuantityMeasurementDatabaseRepository repository;

    @BeforeEach
    void setUp() {
        PostgreSqlTestSupport.ensureDatabase("repo_test");
        System.setProperty("quantity.measurement.database.url", PostgreSqlTestSupport.jdbcUrl("repo_test"));
        System.setProperty("quantity.measurement.database.username", "postgres");
        System.setProperty("quantity.measurement.database.password", "");
        repository = new QuantityMeasurementDatabaseRepository(ApplicationConfig.load());
        repository.deleteAllMeasurements();
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
    void testDatabaseRepositorySaveEntity() {
        QuantityMeasurementEntity entity = new QuantityMeasurementEntity(
                "ADD",
                new QuantityDTO(1.0, "FEET", "LENGTH"),
                new QuantityDTO(12.0, "INCH", "LENGTH"),
                new QuantityDTO(2.0, "FEET", "LENGTH"));

        repository.save(entity);

        List<QuantityMeasurementEntity> storedEntities = repository.getAllMeasurements();
        assertEquals(1, storedEntities.size());
        assertEquals("ADD", storedEntities.getFirst().getOperationType());
    }

    @Test
    void testDatabaseRepositoryQueryByOperationAndType() {
        repository.save(new QuantityMeasurementEntity(
                "COMPARE",
                new QuantityDTO(1.0, "FEET", "LENGTH"),
                new QuantityDTO(12.0, "INCH", "LENGTH"),
                true, null));
        repository.save(new QuantityMeasurementEntity(
                "CONVERT",
                new QuantityDTO(100.0, "CELSIUS", "TEMPERATURE"),
                new QuantityDTO(212.0, "FAHRENHEIT", "TEMPERATURE")));

        assertEquals(1, repository.getMeasurementsByOperation("COMPARE").size());
        assertEquals(1, repository.getMeasurementsByMeasurementType("TEMPERATURE").size());
        assertEquals(2, repository.getTotalCount());
    }

    @Test
    void testDatabaseRepositoryDeleteAllAndPoolStatistics() {
        repository.save(new QuantityMeasurementEntity(
                "DIVIDE",
                new QuantityDTO(24.0, "INCH", "LENGTH"),
                new QuantityDTO(2.0, "FEET", "LENGTH"),
                null, 1.0));

        assertTrue(repository.getPoolStatistics().contains("total="));
        repository.deleteAllMeasurements();
        assertEquals(0, repository.getTotalCount());
    }
}
