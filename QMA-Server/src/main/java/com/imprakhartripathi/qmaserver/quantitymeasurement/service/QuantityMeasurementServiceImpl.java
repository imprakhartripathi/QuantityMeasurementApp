package com.imprakhartripathi.qmaserver.quantitymeasurement.service;

import com.imprakhartripathi.qmaserver.equality.IMeasurable;
import com.imprakhartripathi.qmaserver.equality.LengthUnit;
import com.imprakhartripathi.qmaserver.equality.Quantity;
import com.imprakhartripathi.qmaserver.equality.TemperatureUnit;
import com.imprakhartripathi.qmaserver.equality.VolumeUnit;
import com.imprakhartripathi.qmaserver.equality.WeightUnit;
import com.imprakhartripathi.qmaserver.quantitymeasurement.exception.QuantityMeasurementException;
import com.imprakhartripathi.qmaserver.quantitymeasurement.model.QuantityDTO;
import com.imprakhartripathi.qmaserver.quantitymeasurement.model.QuantityMeasurementEntity;
import com.imprakhartripathi.qmaserver.quantitymeasurement.model.QuantityModel;
import com.imprakhartripathi.qmaserver.quantitymeasurement.repository.IQuantityMeasurementRepository;

import java.util.Objects;

public class QuantityMeasurementServiceImpl implements IQuantityMeasurementService {
    private final IQuantityMeasurementRepository repository;

    public QuantityMeasurementServiceImpl(IQuantityMeasurementRepository repository) {
        this.repository = Objects.requireNonNull(repository, "Repository must not be null");
    }

    @Override
    public QuantityMeasurementEntity compare(QuantityDTO leftQuantity, QuantityDTO rightQuantity) {
        return execute("COMPARE", leftQuantity, rightQuantity, () -> {
            boolean isEqual = compareQuantities(toModel(leftQuantity), toModel(rightQuantity));
            return new QuantityMeasurementEntity("COMPARE", leftQuantity, rightQuantity, isEqual, null);
        });
    }

    @Override
    public QuantityMeasurementEntity convert(QuantityDTO sourceQuantity, String targetUnitName) {
        return execute("CONVERT", sourceQuantity, null, () -> {
            QuantityModel<?> sourceModel = toModel(sourceQuantity);
            IMeasurable targetUnit = resolveTargetUnit(sourceModel.getUnit().getMeasurementType(), targetUnitName);
            Quantity<?> result = convertTo(sourceModel, targetUnit);
            return new QuantityMeasurementEntity("CONVERT", sourceQuantity, toDto(result));
        });
    }

    @Override
    public QuantityMeasurementEntity add(QuantityDTO leftQuantity, QuantityDTO rightQuantity) {
        return add(leftQuantity, rightQuantity, leftQuantity.getUnitName());
    }

    @Override
    public QuantityMeasurementEntity add(QuantityDTO leftQuantity, QuantityDTO rightQuantity, String targetUnitName) {
        return execute("ADD", leftQuantity, rightQuantity, () -> {
            Quantity<?> result = addQuantities(toModel(leftQuantity), toModel(rightQuantity), targetUnitName);
            return new QuantityMeasurementEntity("ADD", leftQuantity, rightQuantity, toDto(result));
        });
    }

    @Override
    public QuantityMeasurementEntity subtract(QuantityDTO leftQuantity, QuantityDTO rightQuantity) {
        return subtract(leftQuantity, rightQuantity, leftQuantity.getUnitName());
    }

    @Override
    public QuantityMeasurementEntity subtract(QuantityDTO leftQuantity, QuantityDTO rightQuantity, String targetUnitName) {
        return execute("SUBTRACT", leftQuantity, rightQuantity, () -> {
            Quantity<?> result = subtractQuantities(toModel(leftQuantity), toModel(rightQuantity), targetUnitName);
            return new QuantityMeasurementEntity("SUBTRACT", leftQuantity, rightQuantity, toDto(result));
        });
    }

    @Override
    public QuantityMeasurementEntity divide(QuantityDTO leftQuantity, QuantityDTO rightQuantity) {
        return execute("DIVIDE", leftQuantity, rightQuantity, () -> {
            double result = divideQuantities(toModel(leftQuantity), toModel(rightQuantity));
            return new QuantityMeasurementEntity("DIVIDE", leftQuantity, rightQuantity, null, result);
        });
    }

    private QuantityMeasurementEntity execute(String operationType, QuantityDTO leftQuantity, QuantityDTO rightQuantity,
                                              Operation operation) {
        validateDto(leftQuantity);
        if (!"CONVERT".equals(operationType)) {
            validateDto(rightQuantity);
        }
        try {
            QuantityMeasurementEntity entity = operation.perform();
            return repository.save(entity);
        } catch (RuntimeException exception) {
            QuantityMeasurementEntity errorEntity = new QuantityMeasurementEntity(operationType, leftQuantity,
                    rightQuantity, mapException(exception).getMessage());
            return repository.save(errorEntity);
        }
    }

    private void validateDto(QuantityDTO quantityDTO) {
        if (quantityDTO == null) {
            throw new QuantityMeasurementException("Quantity input must not be null");
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

    private QuantityMeasurementException mapException(RuntimeException exception) {
        if (exception instanceof QuantityMeasurementException quantityMeasurementException) {
            return quantityMeasurementException;
        }
        return new QuantityMeasurementException(exception.getMessage(), exception);
    }

    @FunctionalInterface
    private interface Operation {
        QuantityMeasurementEntity perform();
    }
}
