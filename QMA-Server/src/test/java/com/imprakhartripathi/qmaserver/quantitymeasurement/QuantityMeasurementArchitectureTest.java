package com.imprakhartripathi.qmaserver.quantitymeasurement;

import com.imprakhartripathi.qmaserver.quantitymeasurement.controller.QuantityMeasurementController;
import com.imprakhartripathi.qmaserver.quantitymeasurement.model.QuantityDTO;
import com.imprakhartripathi.qmaserver.quantitymeasurement.model.QuantityMeasurementEntity;
import com.imprakhartripathi.qmaserver.quantitymeasurement.repository.QuantityMeasurementCacheRepository;
import com.imprakhartripathi.qmaserver.quantitymeasurement.service.QuantityMeasurementServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class QuantityMeasurementArchitectureTest {

    private QuantityMeasurementController controller;

    @BeforeEach
    void setUp() {
        QuantityMeasurementCacheRepository repository = QuantityMeasurementCacheRepository.getInstance();
        repository.clear();
        controller = new QuantityMeasurementController(new QuantityMeasurementServiceImpl(repository));
    }

    @Test
    void testServiceCompareEquality_DifferentUnit_Success() {
        QuantityMeasurementEntity entity = controller.performComparison(
                new QuantityDTO(1.0, "FEET", "LENGTH"),
                new QuantityDTO(12.0, "INCH", "LENGTH"));

        assertFalse(entity.hasError());
        assertTrue(entity.getComparisonResult());
    }

    @Test
    void testServiceConvert_Success() {
        QuantityMeasurementEntity entity = controller.performConversion(
                new QuantityDTO(100.0, "CELSIUS", "TEMPERATURE"), "FAHRENHEIT");

        assertFalse(entity.hasError());
        assertNotNull(entity.getResultQuantity());
        assertEquals(212.0, entity.getResultQuantity().getValue(), 1e-6);
        assertEquals("FAHRENHEIT", entity.getResultQuantity().getUnitName());
    }

    @Test
    void testServiceAdd_UnsupportedOperation_Error() {
        QuantityMeasurementEntity entity = controller.performAddition(
                new QuantityDTO(100.0, "CELSIUS", "TEMPERATURE"),
                new QuantityDTO(50.0, "CELSIUS", "TEMPERATURE"));

        assertTrue(entity.hasError());
        assertEquals("Temperature does not support add", entity.getErrorMessage());
    }

    @Test
    void testControllerDisplayResult_Success() {
        QuantityMeasurementEntity entity = controller.performSubtraction(
                new QuantityDTO(10.0, "FEET", "LENGTH"),
                new QuantityDTO(6.0, "INCH", "LENGTH"));

        assertEquals("SUBTRACT: 9.5 FEET", controller.displayResult(entity));
    }

    @Test
    void testRepositoryStoresMeasurements() {
        QuantityMeasurementCacheRepository repository = QuantityMeasurementCacheRepository.getInstance();
        controller.performDivision(
                new QuantityDTO(24.0, "INCH", "LENGTH"),
                new QuantityDTO(2.0, "FEET", "LENGTH"));

        assertEquals(1, repository.getAllMeasurements().size());
    }
}
