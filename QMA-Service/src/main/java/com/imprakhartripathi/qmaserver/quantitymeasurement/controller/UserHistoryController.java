package com.imprakhartripathi.qmaserver.quantitymeasurement.controller;

import com.imprakhartripathi.qmaserver.quantitymeasurement.model.QuantityMeasurementDTO;
import com.imprakhartripathi.qmaserver.quantitymeasurement.service.IQuantityMeasurementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/users", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "User History", description = "User-scoped quantity measurement history API")
public class UserHistoryController {
    private final IQuantityMeasurementService quantityMeasurementService;

    public UserHistoryController(IQuantityMeasurementService quantityMeasurementService) {
        this.quantityMeasurementService = quantityMeasurementService;
    }

    @GetMapping("/me/history")
    @Operation(summary = "Get history for the authenticated user")
    public List<QuantityMeasurementDTO> getMyHistory(
            @RequestHeader(value = "X-User-Email", required = false) String userEmail) {
        return quantityMeasurementService.getHistoryForUser(userEmail);
    }
}
