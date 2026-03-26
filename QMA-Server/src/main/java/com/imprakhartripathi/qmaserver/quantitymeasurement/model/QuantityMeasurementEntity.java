package com.imprakhartripathi.qmaserver.quantitymeasurement.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.FetchType;

import java.time.LocalDateTime;

@Entity
@Table(name = "quantity_measurements",
        indexes = {
                @Index(name = "idx_quantity_measurements_operation", columnList = "operation_type"),
                @Index(name = "idx_quantity_measurements_measurement", columnList = "left_measurement_type"),
                @Index(name = "idx_quantity_measurements_created_at", columnList = "created_at")
        })
public class QuantityMeasurementEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "operation_type", nullable = false, length = 32)
    private OperationType operationType;

    @Column(name = "left_value", nullable = false)
    private Double leftValue;

    @Column(name = "left_unit", nullable = false, length = 32)
    private String leftUnit;

    @Column(name = "left_measurement_type", nullable = false, length = 32)
    private String leftMeasurementType;

    @Column(name = "right_value")
    private Double rightValue;

    @Column(name = "right_unit", length = 32)
    private String rightUnit;

    @Column(name = "right_measurement_type", length = 32)
    private String rightMeasurementType;

    @Column(name = "result_string", length = 255)
    private String resultString;

    @Column(name = "result_value")
    private Double resultValue;

    @Column(name = "result_unit", length = 32)
    private String resultUnit;

    @Column(name = "result_measurement_type", length = 32)
    private String resultMeasurementType;

    @Column(name = "comparison_result")
    private Boolean comparisonResult;

    @Column(name = "scalar_result")
    private Double scalarResult;

    @Column(name = "is_error", nullable = false)
    private boolean error;

    @Column(name = "error_message", length = 500)
    private String errorMessage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    public QuantityMeasurementEntity() {
    }

    public QuantityMeasurementEntity(OperationType operationType, QuantityDTO leftOperand, QuantityDTO resultQuantity) {
        this.operationType = operationType;
        applyLeftOperand(leftOperand);
        applyResultQuantity(resultQuantity);
        this.error = false;
    }

    public QuantityMeasurementEntity(OperationType operationType, QuantityDTO leftOperand, QuantityDTO rightOperand,
                                     QuantityDTO resultQuantity) {
        this(operationType, leftOperand, resultQuantity);
        applyRightOperand(rightOperand);
    }

    public QuantityMeasurementEntity(OperationType operationType, QuantityDTO leftOperand, QuantityDTO rightOperand,
                                     Boolean comparisonResult, Double scalarResult) {
        this.operationType = operationType;
        applyLeftOperand(leftOperand);
        applyRightOperand(rightOperand);
        this.comparisonResult = comparisonResult;
        this.scalarResult = scalarResult;
        this.resultString = comparisonResult != null ? String.valueOf(comparisonResult) : null;
        this.resultValue = scalarResult;
        this.error = false;
    }

    public QuantityMeasurementEntity(OperationType operationType, QuantityDTO leftOperand, QuantityDTO rightOperand,
                                     String errorMessage) {
        this.operationType = operationType;
        applyLeftOperand(leftOperand);
        applyRightOperand(rightOperand);
        this.error = true;
        this.errorMessage = errorMessage;
    }

    @PrePersist
    void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        if (createdAt == null) {
            createdAt = now;
        }
        updatedAt = now;
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public Double getLeftValue() {
        return leftValue;
    }

    public void setLeftValue(Double leftValue) {
        this.leftValue = leftValue;
    }

    public String getLeftUnit() {
        return leftUnit;
    }

    public void setLeftUnit(String leftUnit) {
        this.leftUnit = leftUnit;
    }

    public String getLeftMeasurementType() {
        return leftMeasurementType;
    }

    public void setLeftMeasurementType(String leftMeasurementType) {
        this.leftMeasurementType = leftMeasurementType;
    }

    public Double getRightValue() {
        return rightValue;
    }

    public void setRightValue(Double rightValue) {
        this.rightValue = rightValue;
    }

    public String getRightUnit() {
        return rightUnit;
    }

    public void setRightUnit(String rightUnit) {
        this.rightUnit = rightUnit;
    }

    public String getRightMeasurementType() {
        return rightMeasurementType;
    }

    public void setRightMeasurementType(String rightMeasurementType) {
        this.rightMeasurementType = rightMeasurementType;
    }

    public String getResultString() {
        return resultString;
    }

    public void setResultString(String resultString) {
        this.resultString = resultString;
    }

    public Double getResultValue() {
        return resultValue;
    }

    public void setResultValue(Double resultValue) {
        this.resultValue = resultValue;
    }

    public String getResultUnit() {
        return resultUnit;
    }

    public void setResultUnit(String resultUnit) {
        this.resultUnit = resultUnit;
    }

    public String getResultMeasurementType() {
        return resultMeasurementType;
    }

    public void setResultMeasurementType(String resultMeasurementType) {
        this.resultMeasurementType = resultMeasurementType;
    }

    public Boolean getComparisonResult() {
        return comparisonResult;
    }

    public void setComparisonResult(Boolean comparisonResult) {
        this.comparisonResult = comparisonResult;
    }

    public Double getScalarResult() {
        return scalarResult;
    }

    public void setScalarResult(Double scalarResult) {
        this.scalarResult = scalarResult;
    }

    public boolean isError() {
        return error;
    }

    public boolean hasError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public QuantityDTO getLeftOperand() {
        return new QuantityDTO(leftValue, leftUnit, leftMeasurementType);
    }

    public QuantityDTO getRightOperand() {
        if (rightValue == null || rightUnit == null || rightMeasurementType == null) {
            return null;
        }
        return new QuantityDTO(rightValue, rightUnit, rightMeasurementType);
    }

    public QuantityDTO getResultQuantity() {
        if (resultValue == null || resultUnit == null || resultMeasurementType == null) {
            return null;
        }
        return new QuantityDTO(resultValue, resultUnit, resultMeasurementType);
    }

    private void applyLeftOperand(QuantityDTO quantityDTO) {
        this.leftValue = quantityDTO.getValue();
        this.leftUnit = quantityDTO.getUnitName();
        this.leftMeasurementType = quantityDTO.getMeasurementType();
    }

    private void applyRightOperand(QuantityDTO quantityDTO) {
        if (quantityDTO == null) {
            return;
        }
        this.rightValue = quantityDTO.getValue();
        this.rightUnit = quantityDTO.getUnitName();
        this.rightMeasurementType = quantityDTO.getMeasurementType();
    }

    private void applyResultQuantity(QuantityDTO quantityDTO) {
        if (quantityDTO == null) {
            return;
        }
        this.resultValue = quantityDTO.getValue();
        this.resultUnit = quantityDTO.getUnitName();
        this.resultMeasurementType = quantityDTO.getMeasurementType();
    }
}
