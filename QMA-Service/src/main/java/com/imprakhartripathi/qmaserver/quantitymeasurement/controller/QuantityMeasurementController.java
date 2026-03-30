package com.imprakhartripathi.qmaserver.quantitymeasurement.controller;

import com.imprakhartripathi.qmaserver.quantitymeasurement.model.OperationType;
import com.imprakhartripathi.qmaserver.quantitymeasurement.model.QuantityInputDTO;
import com.imprakhartripathi.qmaserver.quantitymeasurement.model.QuantityMeasurementDTO;
import com.imprakhartripathi.qmaserver.quantitymeasurement.service.IQuantityMeasurementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/quantities", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Quantity Measurements", description = "REST API for quantity measurement operations")
public class QuantityMeasurementController {
    private final IQuantityMeasurementService quantityMeasurementService;

    public QuantityMeasurementController(IQuantityMeasurementService quantityMeasurementService) {
        this.quantityMeasurementService = quantityMeasurementService;
    }

    @PostMapping(value = "/compare", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Compare two quantities")
    public QuantityMeasurementDTO compareQuantities(
            @RequestHeader(value = "X-User-Email", required = false) String userEmail,
            @Valid @RequestBody QuantityInputDTO input) {
        return quantityMeasurementService.compare(input.getThisQuantityDTO(), input.getThatQuantityDTO(), userEmail);
    }

    @PostMapping(value = "/convert", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Convert a quantity to another unit")
    public QuantityMeasurementDTO convertQuantity(
            @RequestHeader(value = "X-User-Email", required = false) String userEmail,
            @Valid @RequestBody QuantityInputDTO input) {
        return quantityMeasurementService.convert(
                input.getThisQuantityDTO(),
                input.getThatQuantityDTO().getUnitName(),
                userEmail);
    }

    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Add two quantities")
    public QuantityMeasurementDTO addQuantities(
            @RequestHeader(value = "X-User-Email", required = false) String userEmail,
            @Valid @RequestBody QuantityInputDTO input) {
        return quantityMeasurementService.add(input.getThisQuantityDTO(), input.getThatQuantityDTO(),
                input.getThisQuantityDTO().getUnitName(), userEmail);
    }

    @PostMapping(value = "/subtract", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Subtract two quantities")
    public QuantityMeasurementDTO subtractQuantities(
            @RequestHeader(value = "X-User-Email", required = false) String userEmail,
            @Valid @RequestBody QuantityInputDTO input) {
        return quantityMeasurementService.subtract(input.getThisQuantityDTO(), input.getThatQuantityDTO(),
                input.getThisQuantityDTO().getUnitName(), userEmail);
    }

    @PostMapping(value = "/divide", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Divide two quantities")
    public QuantityMeasurementDTO divideQuantities(
            @RequestHeader(value = "X-User-Email", required = false) String userEmail,
            @Valid @RequestBody QuantityInputDTO input) {
        return quantityMeasurementService.divide(input.getThisQuantityDTO(), input.getThatQuantityDTO(), userEmail);
    }

    @GetMapping("/history/operation/{operation}")
    @Operation(summary = "Get quantity measurement history by operation")
    public List<QuantityMeasurementDTO> getOperationHistory(@PathVariable String operation) {
        return quantityMeasurementService.getOperationHistory(OperationType.from(operation));
    }

    @GetMapping("/history/type/{measurementType}")
    @Operation(summary = "Get quantity measurement history by measurement type")
    public List<QuantityMeasurementDTO> getMeasurementHistory(@PathVariable String measurementType) {
        return quantityMeasurementService.getMeasurementHistory(measurementType);
    }

    @GetMapping("/history/errored")
    @Operation(summary = "Get errored quantity measurement history")
    public List<QuantityMeasurementDTO> getErroredHistory() {
        return quantityMeasurementService.getErroredHistory();
    }

    @GetMapping("/count/{operation}")
    @Operation(summary = "Get successful operation count")
    public long getOperationCount(@PathVariable String operation) {
        return quantityMeasurementService.getOperationCount(OperationType.from(operation));
    }
}
