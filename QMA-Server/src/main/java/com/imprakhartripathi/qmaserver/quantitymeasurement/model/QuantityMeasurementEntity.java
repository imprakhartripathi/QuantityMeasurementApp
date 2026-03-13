package com.imprakhartripathi.qmaserver.quantitymeasurement.model;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class QuantityMeasurementEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final String id;
    private final LocalDateTime createdAt;
    private final String operationType;
    private final QuantityDTO leftOperand;
    private final QuantityDTO rightOperand;
    private final QuantityDTO resultQuantity;
    private final Boolean comparisonResult;
    private final Double scalarResult;
    private final boolean error;
    private final String errorMessage;

    public QuantityMeasurementEntity(String operationType, QuantityDTO leftOperand, QuantityDTO resultQuantity) {
        this(UUID.randomUUID().toString(), LocalDateTime.now(), operationType, leftOperand, null,
                resultQuantity, null, null, false, null);
    }

    public QuantityMeasurementEntity(String operationType, QuantityDTO leftOperand, QuantityDTO rightOperand,
                                     QuantityDTO resultQuantity) {
        this(UUID.randomUUID().toString(), LocalDateTime.now(), operationType, leftOperand, rightOperand,
                resultQuantity, null, null, false, null);
    }

    public QuantityMeasurementEntity(String operationType, QuantityDTO leftOperand, QuantityDTO rightOperand,
                                     Boolean comparisonResult, Double scalarResult) {
        this(UUID.randomUUID().toString(), LocalDateTime.now(), operationType, leftOperand, rightOperand,
                null, comparisonResult, scalarResult, false, null);
    }

    public QuantityMeasurementEntity(String operationType, QuantityDTO leftOperand, QuantityDTO rightOperand,
                                     String errorMessage) {
        this(UUID.randomUUID().toString(), LocalDateTime.now(), operationType, leftOperand, rightOperand,
                null, null, null, true,
                Objects.requireNonNull(errorMessage, "Error message must not be null"));
    }

    public static QuantityMeasurementEntity restore(String id, LocalDateTime createdAt, String operationType,
                                                    QuantityDTO leftOperand, QuantityDTO rightOperand,
                                                    QuantityDTO resultQuantity, Boolean comparisonResult,
                                                    Double scalarResult, boolean error, String errorMessage) {
        return new QuantityMeasurementEntity(id, createdAt, operationType, leftOperand, rightOperand, resultQuantity,
                comparisonResult, scalarResult, error, errorMessage);
    }

    private QuantityMeasurementEntity(String id, LocalDateTime createdAt, String operationType,
                                      QuantityDTO leftOperand, QuantityDTO rightOperand, QuantityDTO resultQuantity,
                                      Boolean comparisonResult, Double scalarResult, boolean error, String errorMessage) {
        this.id = Objects.requireNonNull(id, "Id must not be null");
        this.createdAt = Objects.requireNonNull(createdAt, "Created at must not be null");
        this.operationType = Objects.requireNonNull(operationType, "Operation type must not be null");
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
        this.resultQuantity = resultQuantity;
        this.comparisonResult = comparisonResult;
        this.scalarResult = scalarResult;
        this.error = error;
        this.errorMessage = errorMessage;
    }

    public String getId() {
        return id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getOperationType() {
        return operationType;
    }

    public QuantityDTO getLeftOperand() {
        return leftOperand;
    }

    public QuantityDTO getRightOperand() {
        return rightOperand;
    }

    public QuantityDTO getResultQuantity() {
        return resultQuantity;
    }

    public Boolean getComparisonResult() {
        return comparisonResult;
    }

    public Double getScalarResult() {
        return scalarResult;
    }

    public boolean hasError() {
        return error;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public String toString() {
        if (error) {
            return "QuantityMeasurementEntity{" +
                    "operationType='" + operationType + '\'' +
                    ", errorMessage='" + errorMessage + '\'' +
                    '}';
        }
        if (comparisonResult != null) {
            return "QuantityMeasurementEntity{" +
                    "operationType='" + operationType + '\'' +
                    ", comparisonResult=" + comparisonResult +
                    '}';
        }
        if (scalarResult != null) {
            return "QuantityMeasurementEntity{" +
                    "operationType='" + operationType + '\'' +
                    ", scalarResult=" + scalarResult +
                    '}';
        }
        return "QuantityMeasurementEntity{" +
                "operationType='" + operationType + '\'' +
                ", resultQuantity=" + resultQuantity +
                '}';
    }
}
