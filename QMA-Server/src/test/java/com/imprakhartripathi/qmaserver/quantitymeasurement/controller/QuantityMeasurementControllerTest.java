package com.imprakhartripathi.qmaserver.quantitymeasurement.controller;

import com.imprakhartripathi.qmaserver.quantitymeasurement.model.QuantityDTO;
import com.imprakhartripathi.qmaserver.quantitymeasurement.model.QuantityMeasurementEntity;
import com.imprakhartripathi.qmaserver.quantitymeasurement.service.IQuantityMeasurementService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class QuantityMeasurementControllerTest {

    @Test
    void testControllerDelegatesToService() {
        IQuantityMeasurementService service = mock(IQuantityMeasurementService.class);
        QuantityMeasurementController controller = new QuantityMeasurementController(service);
        QuantityDTO left = new QuantityDTO(1.0, "FEET", "LENGTH");
        QuantityDTO right = new QuantityDTO(12.0, "INCH", "LENGTH");
        QuantityMeasurementEntity entity = new QuantityMeasurementEntity("COMPARE", left, right, true, null);

        when(service.compare(left, right)).thenReturn(entity);

        QuantityMeasurementEntity result = controller.performComparison(left, right);

        assertEquals(entity, result);
        verify(service).compare(left, right);
    }
}
