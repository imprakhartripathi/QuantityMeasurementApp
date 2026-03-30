package com.imprakhartripathi.qmaserver.quantitymeasurement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imprakhartripathi.qmaserver.quantitymeasurement.exception.GlobalExceptionHandler;
import com.imprakhartripathi.qmaserver.quantitymeasurement.model.OperationType;
import com.imprakhartripathi.qmaserver.quantitymeasurement.model.QuantityDTO;
import com.imprakhartripathi.qmaserver.quantitymeasurement.model.QuantityInputDTO;
import com.imprakhartripathi.qmaserver.quantitymeasurement.model.QuantityMeasurementDTO;
import com.imprakhartripathi.qmaserver.quantitymeasurement.service.IQuantityMeasurementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class QuantityMeasurementControllerTest {
    private MockMvc mockMvc;
    private IQuantityMeasurementService quantityMeasurementService;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        quantityMeasurementService = mock(IQuantityMeasurementService.class);
        QuantityMeasurementController controller = new QuantityMeasurementController(quantityMeasurementService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testRestEndpointCompareQuantities() throws Exception {
        QuantityInputDTO input = input("FEET", "LENGTH", "INCH", "LENGTH");
        when(quantityMeasurementService.compare(any(QuantityDTO.class), any(QuantityDTO.class), isNull()))
                .thenReturn(measurement("COMPARE", "true", null, null, null, false, null));

        mockMvc.perform(post("/api/v1/quantities/compare")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operation").value("COMPARE"))
                .andExpect(jsonPath("$.resultString").value("true"));
    }

    @Test
    void testRestEndpointInvalidInputReturns400() throws Exception {
        String invalidBody = """
                {
                  "thisQuantityDTO": {"value": 1.0, "unit": "FOOT", "measurementType": "LENGTH"},
                  "thatQuantityDTO": {"value": 12.0, "unit": "INCH", "measurementType": "LENGTH"}
                }
                """;

        mockMvc.perform(post("/api/v1/quantities/compare")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Quantity Measurement Error"));
    }

    @Test
    void testRequestPathVariableExtraction() throws Exception {
        when(quantityMeasurementService.getOperationCount(eq(OperationType.COMPARE))).thenReturn(2L);

        mockMvc.perform(get("/api/v1/quantities/count/COMPARE"))
                .andExpect(status().isOk())
                .andExpect(content().string("2"));
    }

    private QuantityInputDTO input(String thisUnit, String thisType, String thatUnit, String thatType) {
        QuantityInputDTO input = new QuantityInputDTO();
        input.setThisQuantityDTO(new QuantityDTO(1.0, thisUnit, thisType));
        input.setThatQuantityDTO(new QuantityDTO(12.0, thatUnit, thatType));
        return input;
    }

    private QuantityMeasurementDTO measurement(String operation, String resultString, Double resultValue,
                                               String resultUnit, String resultMeasurementType,
                                               boolean error, String errorMessage) {
        QuantityMeasurementEntityFixture entity = new QuantityMeasurementEntityFixture(operation, resultString,
                resultValue, resultUnit, resultMeasurementType, error, errorMessage);
        return QuantityMeasurementDTO.fromEntity(entity.toEntity());
    }

    private record QuantityMeasurementEntityFixture(String operation, String resultString, Double resultValue,
                                                    String resultUnit, String resultMeasurementType,
                                                    boolean error, String errorMessage) {
        private com.imprakhartripathi.qmaserver.quantitymeasurement.model.QuantityMeasurementEntity toEntity() {
            com.imprakhartripathi.qmaserver.quantitymeasurement.model.QuantityMeasurementEntity entity =
                    new com.imprakhartripathi.qmaserver.quantitymeasurement.model.QuantityMeasurementEntity();
            entity.setOperationType(OperationType.from(operation));
            entity.setLeftValue(1.0);
            entity.setLeftUnit("FEET");
            entity.setLeftMeasurementType("LENGTH");
            entity.setResultString(resultString);
            entity.setResultValue(resultValue);
            entity.setResultUnit(resultUnit);
            entity.setResultMeasurementType(resultMeasurementType);
            entity.setError(error);
            entity.setErrorMessage(errorMessage);
            return entity;
        }
    }
}
