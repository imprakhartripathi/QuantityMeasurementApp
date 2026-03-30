package com.imprakhartripathi.qmaserver.quantitymeasurement.service;

import com.imprakhartripathi.qmaserver.equality.IMeasurable;
import com.imprakhartripathi.qmaserver.equality.LengthUnit;
import com.imprakhartripathi.qmaserver.equality.Quantity;
import com.imprakhartripathi.qmaserver.equality.TemperatureUnit;
import com.imprakhartripathi.qmaserver.equality.VolumeUnit;
import com.imprakhartripathi.qmaserver.equality.WeightUnit;
import com.imprakhartripathi.qmaserver.quantitymeasurement.exception.QuantityMeasurementException;
import com.imprakhartripathi.qmaserver.quantitymeasurement.model.OperationType;
import com.imprakhartripathi.qmaserver.quantitymeasurement.model.QuantityDTO;
import com.imprakhartripathi.qmaserver.quantitymeasurement.model.QuantityMeasurementDTO;
import com.imprakhartripathi.qmaserver.quantitymeasurement.model.QuantityMeasurementEntity;
import com.imprakhartripathi.qmaserver.quantitymeasurement.model.QuantityModel;
import com.imprakhartripathi.qmaserver.quantitymeasurement.repository.QuantityMeasurementRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class QuantityMeasurementServiceImpl implements IQuantityMeasurementService {
    private final QuantityMeasurementRepository repository;

    public QuantityMeasurementServiceImpl(QuantityMeasurementRepository repository) {
        this.repository = repository;
    }

    @Override
    public QuantityMeasurementDTO compare(QuantityDTO leftQuantity, QuantityDTO rightQuantity, String userEmail) {
        return execute(OperationType.COMPARE, leftQuantity, rightQuantity, userEmail, () -> {
            boolean isEqual = compareQuantities(toModel(leftQuantity), toModel(rightQuantity));
            return new QuantityMeasurementEntity(OperationType.COMPARE, leftQuantity, rightQuantity, isEqual, null);
        });
    }

    @Override
    public QuantityMeasurementDTO convert(QuantityDTO sourceQuantity, String targetUnitName, String userEmail) {
        return execute(OperationType.CONVERT, sourceQuantity, null, userEmail, () -> {
            QuantityModel<?> sourceModel = toModel(sourceQuantity);
            IMeasurable targetUnit = resolveTargetUnit(sourceModel.getUnit().getMeasurementType(), targetUnitName);
            Quantity<?> result = convertTo(sourceModel, targetUnit);
            return new QuantityMeasurementEntity(OperationType.CONVERT, sourceQuantity, toDto(result));
        });
    }

    @Override
    public QuantityMeasurementDTO add(QuantityDTO leftQuantity, QuantityDTO rightQuantity) {
        return add(leftQuantity, rightQuantity, leftQuantity.getUnitName(), null);
    }

    @Override
    public QuantityMeasurementDTO add(QuantityDTO leftQuantity, QuantityDTO rightQuantity, String targetUnitName) {
        return add(leftQuantity, rightQuantity, targetUnitName, null);
    }

    @Override
    public QuantityMeasurementDTO add(QuantityDTO leftQuantity, QuantityDTO rightQuantity,
                                      String targetUnitName, String userEmail) {
        return execute(OperationType.ADD, leftQuantity, rightQuantity, userEmail, () -> {
            Quantity<?> result = addQuantities(toModel(leftQuantity), toModel(rightQuantity), targetUnitName);
            return new QuantityMeasurementEntity(OperationType.ADD, leftQuantity, rightQuantity, toDto(result));
        });
    }

    @Override
    public QuantityMeasurementDTO subtract(QuantityDTO leftQuantity, QuantityDTO rightQuantity) {
        return subtract(leftQuantity, rightQuantity, leftQuantity.getUnitName(), null);
    }

    @Override
    public QuantityMeasurementDTO subtract(QuantityDTO leftQuantity, QuantityDTO rightQuantity, String targetUnitName) {
        return subtract(leftQuantity, rightQuantity, targetUnitName, null);
    }

    @Override
    public QuantityMeasurementDTO subtract(QuantityDTO leftQuantity, QuantityDTO rightQuantity,
                                           String targetUnitName, String userEmail) {
        return execute(OperationType.SUBTRACT, leftQuantity, rightQuantity, userEmail, () -> {
            Quantity<?> result = subtractQuantities(toModel(leftQuantity), toModel(rightQuantity), targetUnitName);
            return new QuantityMeasurementEntity(OperationType.SUBTRACT, leftQuantity, rightQuantity, toDto(result));
        });
    }

    @Override
    public QuantityMeasurementDTO divide(QuantityDTO leftQuantity, QuantityDTO rightQuantity, String userEmail) {
        return execute(OperationType.DIVIDE, leftQuantity, rightQuantity, userEmail, () -> {
            double result = divideQuantities(toModel(leftQuantity), toModel(rightQuantity));
            return new QuantityMeasurementEntity(OperationType.DIVIDE, leftQuantity, rightQuantity, null, result);
        });
    }

    @Override
    public List<QuantityMeasurementDTO> getOperationHistory(OperationType operationType) {
        return QuantityMeasurementDTO.fromEntityList(repository.findByOperationTypeOrderByCreatedAtAsc(operationType));
    }

    @Override
    public List<QuantityMeasurementDTO> getMeasurementHistory(String measurementType) {
        return QuantityMeasurementDTO.fromEntityList(
                repository.findByLeftMeasurementTypeOrderByCreatedAtAsc(measurementType.trim().toUpperCase()));
    }

    @Override
    public long getOperationCount(OperationType operationType) {
        return repository.countByOperationTypeAndErrorFalse(operationType);
    }

    @Override
    public List<QuantityMeasurementDTO> getErroredHistory() {
        return QuantityMeasurementDTO.fromEntityList(repository.findByErrorTrueOrderByCreatedAtAsc());
    }

    @Override
    public List<QuantityMeasurementDTO> getHistoryForUser(String userEmail) {
        String normalizedEmail = normalizeUserEmail(userEmail)
                .orElseThrow(() -> new QuantityMeasurementException("Missing authenticated user identity"));
        return QuantityMeasurementDTO.fromEntityList(repository.findByUserEmailIgnoreCaseOrderByCreatedAtDesc(normalizedEmail));
    }

    private QuantityMeasurementDTO execute(OperationType operationType, QuantityDTO leftQuantity, QuantityDTO rightQuantity,
                                           String userEmail, Operation operation) {
        validateDto(leftQuantity);
        if (operationType != OperationType.CONVERT) {
            validateDto(rightQuantity);
        }
        try {
            return saveAndConvert(operation.perform(), userEmail);
        } catch (RuntimeException exception) {
            QuantityMeasurementEntity errorEntity = new QuantityMeasurementEntity(operationType, leftQuantity,
                    rightQuantity, mapException(operationType, exception).getMessage());
            normalizeUserEmail(userEmail).ifPresent(errorEntity::setUserEmail);
            repository.save(errorEntity);
            throw mapException(operationType, exception);
        }
    }

    private QuantityMeasurementDTO saveAndConvert(QuantityMeasurementEntity entity, String userEmail) {
        normalizeUserEmail(userEmail).ifPresent(entity::setUserEmail);
        return QuantityMeasurementDTO.fromEntity(repository.save(entity));
    }

    private Optional<String> normalizeUserEmail(String userEmail) {
        if (userEmail == null || userEmail.isBlank()) {
            return Optional.empty();
        }
        String normalized = userEmail.trim().toLowerCase(Locale.ROOT);
        return normalized.contains("@") ? Optional.of(normalized) : Optional.empty();
    }

    private void validateDto(QuantityDTO quantityDTO) {
        if (quantityDTO == null) {
            throw new QuantityMeasurementException("Quantity input must not be null");
        }
        if (quantityDTO.getValue() == null) {
            throw new QuantityMeasurementException("Quantity value must not be null");
        }
        if (Double.isNaN(quantityDTO.getValue()) || Double.isInfinite(quantityDTO.getValue())) {
            throw new QuantityMeasurementException("Quantity value must be a finite number");
        }
    }

    private QuantityModel<?> toModel(QuantityDTO dto) {
        try {
            IMeasurable unit = IMeasurable.resolveUnit(dto.getMeasurementType(), dto.getUnitName());
            return new QuantityModel<>(dto.getValue(), unit);
        } catch (IllegalArgumentException exception) {
            throw new QuantityMeasurementException("Invalid quantity unit or measurement type", exception);
        }
    }

    private IMeasurable resolveTargetUnit(String measurementType, String targetUnitName) {
        try {
            return IMeasurable.resolveUnit(measurementType, targetUnitName);
        } catch (IllegalArgumentException exception) {
            throw new QuantityMeasurementException("Invalid target unit", exception);
        }
    }

    private Quantity<?> convertTo(QuantityModel<?> sourceModel, IMeasurable targetUnit) {
        if (sourceModel.getUnit() instanceof LengthUnit source && targetUnit instanceof LengthUnit target) {
            return new Quantity<>(sourceModel.getValue(), source).convertTo(target);
        }
        if (sourceModel.getUnit() instanceof WeightUnit source && targetUnit instanceof WeightUnit target) {
            return new Quantity<>(sourceModel.getValue(), source).convertTo(target);
        }
        if (sourceModel.getUnit() instanceof VolumeUnit source && targetUnit instanceof VolumeUnit target) {
            return new Quantity<>(sourceModel.getValue(), source).convertTo(target);
        }
        if (sourceModel.getUnit() instanceof TemperatureUnit source && targetUnit instanceof TemperatureUnit target) {
            return new Quantity<>(sourceModel.getValue(), source).convertTo(target);
        }
        throw new QuantityMeasurementException("Cannot convert quantities of different categories");
    }

    private Quantity<?> addQuantities(QuantityModel<?> leftModel, QuantityModel<?> rightModel, String targetUnitName) {
        IMeasurable targetUnit = resolveTargetUnit(leftModel.getUnit().getMeasurementType(), targetUnitName);
        if (leftModel.getUnit() instanceof LengthUnit left && rightModel.getUnit() instanceof LengthUnit right
                && targetUnit instanceof LengthUnit target) {
            return new Quantity<>(leftModel.getValue(), left).add(new Quantity<>(rightModel.getValue(), right), target);
        }
        if (leftModel.getUnit() instanceof WeightUnit left && rightModel.getUnit() instanceof WeightUnit right
                && targetUnit instanceof WeightUnit target) {
            return new Quantity<>(leftModel.getValue(), left).add(new Quantity<>(rightModel.getValue(), right), target);
        }
        if (leftModel.getUnit() instanceof VolumeUnit left && rightModel.getUnit() instanceof VolumeUnit right
                && targetUnit instanceof VolumeUnit target) {
            return new Quantity<>(leftModel.getValue(), left).add(new Quantity<>(rightModel.getValue(), right), target);
        }
        if (leftModel.getUnit() instanceof TemperatureUnit left && rightModel.getUnit() instanceof TemperatureUnit right
                && targetUnit instanceof TemperatureUnit target) {
            return new Quantity<>(leftModel.getValue(), left).add(new Quantity<>(rightModel.getValue(), right), target);
        }
        throw new QuantityMeasurementException("Cannot add quantities of different categories");
    }

    private Quantity<?> subtractQuantities(QuantityModel<?> leftModel, QuantityModel<?> rightModel, String targetUnitName) {
        IMeasurable targetUnit = resolveTargetUnit(leftModel.getUnit().getMeasurementType(), targetUnitName);
        if (leftModel.getUnit() instanceof LengthUnit left && rightModel.getUnit() instanceof LengthUnit right
                && targetUnit instanceof LengthUnit target) {
            return new Quantity<>(leftModel.getValue(), left)
                    .subtract(new Quantity<>(rightModel.getValue(), right), target);
        }
        if (leftModel.getUnit() instanceof WeightUnit left && rightModel.getUnit() instanceof WeightUnit right
                && targetUnit instanceof WeightUnit target) {
            return new Quantity<>(leftModel.getValue(), left)
                    .subtract(new Quantity<>(rightModel.getValue(), right), target);
        }
        if (leftModel.getUnit() instanceof VolumeUnit left && rightModel.getUnit() instanceof VolumeUnit right
                && targetUnit instanceof VolumeUnit target) {
            return new Quantity<>(leftModel.getValue(), left)
                    .subtract(new Quantity<>(rightModel.getValue(), right), target);
        }
        if (leftModel.getUnit() instanceof TemperatureUnit left && rightModel.getUnit() instanceof TemperatureUnit right
                && targetUnit instanceof TemperatureUnit target) {
            return new Quantity<>(leftModel.getValue(), left)
                    .subtract(new Quantity<>(rightModel.getValue(), right), target);
        }
        throw new QuantityMeasurementException("Cannot subtract quantities of different categories");
    }

    private double divideQuantities(QuantityModel<?> leftModel, QuantityModel<?> rightModel) {
        if (leftModel.getUnit() instanceof LengthUnit left && rightModel.getUnit() instanceof LengthUnit right) {
            return new Quantity<>(leftModel.getValue(), left).divide(new Quantity<>(rightModel.getValue(), right));
        }
        if (leftModel.getUnit() instanceof WeightUnit left && rightModel.getUnit() instanceof WeightUnit right) {
            return new Quantity<>(leftModel.getValue(), left).divide(new Quantity<>(rightModel.getValue(), right));
        }
        if (leftModel.getUnit() instanceof VolumeUnit left && rightModel.getUnit() instanceof VolumeUnit right) {
            return new Quantity<>(leftModel.getValue(), left).divide(new Quantity<>(rightModel.getValue(), right));
        }
        if (leftModel.getUnit() instanceof TemperatureUnit left && rightModel.getUnit() instanceof TemperatureUnit right) {
            return new Quantity<>(leftModel.getValue(), left).divide(new Quantity<>(rightModel.getValue(), right));
        }
        throw new QuantityMeasurementException("Cannot divide quantities of different categories");
    }

    private QuantityDTO toDto(Quantity<?> quantity) {
        return QuantityDTO.from(quantity.getValue(), quantity.getUnit());
    }

    private boolean compareQuantities(QuantityModel<?> leftModel, QuantityModel<?> rightModel) {
        if (leftModel.getUnit() instanceof LengthUnit left && rightModel.getUnit() instanceof LengthUnit right) {
            return new Quantity<>(leftModel.getValue(), left).equals(new Quantity<>(rightModel.getValue(), right));
        }
        if (leftModel.getUnit() instanceof WeightUnit left && rightModel.getUnit() instanceof WeightUnit right) {
            return new Quantity<>(leftModel.getValue(), left).equals(new Quantity<>(rightModel.getValue(), right));
        }
        if (leftModel.getUnit() instanceof VolumeUnit left && rightModel.getUnit() instanceof VolumeUnit right) {
            return new Quantity<>(leftModel.getValue(), left).equals(new Quantity<>(rightModel.getValue(), right));
        }
        if (leftModel.getUnit() instanceof TemperatureUnit left && rightModel.getUnit() instanceof TemperatureUnit right) {
            return new Quantity<>(leftModel.getValue(), left).equals(new Quantity<>(rightModel.getValue(), right));
        }
        return false;
    }

    private QuantityMeasurementException mapException(OperationType operationType, RuntimeException exception) {
        if (exception instanceof QuantityMeasurementException quantityMeasurementException) {
            return quantityMeasurementException;
        }
        return new QuantityMeasurementException(operationType.name().toLowerCase() + " Error: " + exception.getMessage(),
                exception);
    }

    @FunctionalInterface
    private interface Operation {
        QuantityMeasurementEntity perform();
    }
}
