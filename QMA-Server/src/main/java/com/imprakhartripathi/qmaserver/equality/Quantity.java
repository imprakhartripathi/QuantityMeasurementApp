package com.imprakhartripathi.qmaserver.equality;

import java.util.Objects;

public final class Quantity<U extends IMeasurable> {
    private final double value;
    private final U unit;

    public Quantity(double value, U unit) {
        if (unit == null) {
            throw new IllegalArgumentException("Unit must not be null");
        }
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            throw new IllegalArgumentException("Value must be a finite number");
        }
        this.value = value;
        this.unit = unit;
    }

    public double getValue() {
        return value;
    }

    public U getUnit() {
        return unit;
    }

    private double valueInBaseUnit() {
        return unit.convertToBaseUnit(value);
    }

    private enum ArithmeticOperation {
        ADD {
            @Override
            double compute(double left, double right) {
                return left + right;
            }
        },
        SUBTRACT {
            @Override
            double compute(double left, double right) {
                return left - right;
            }
        },
        DIVIDE {
            @Override
            double compute(double left, double right) {
                if (Double.compare(right, 0.0) == 0) {
                    throw new ArithmeticException("Division by zero");
                }
                return left / right;
            }
        };

        abstract double compute(double left, double right);
    }

    public Quantity<U> convertTo(U targetUnit) {
        if (targetUnit == null) {
            throw new IllegalArgumentException("Target unit must not be null");
        }
        double baseValue = valueInBaseUnit();
        double converted = targetUnit.convertFromBaseUnit(baseValue);
        return new Quantity<>(converted, targetUnit);
    }

    public Quantity<U> add(Quantity<U> other) {
        return add(other, this.unit);
    }

    public Quantity<U> add(Quantity<U> other, U targetUnit) {
        validateArithmeticOperands(other, targetUnit, ArithmeticOperation.ADD, "Quantity to add must not be null");
        return buildResult(other, targetUnit, ArithmeticOperation.ADD, false);
    }

    public Quantity<U> subtract(Quantity<U> other) {
        return subtract(other, this.unit);
    }

    public Quantity<U> subtract(Quantity<U> other, U targetUnit) {
        validateArithmeticOperands(other, targetUnit, ArithmeticOperation.SUBTRACT,
                "Quantity to subtract must not be null");
        return buildResult(other, targetUnit, ArithmeticOperation.SUBTRACT, true);
    }

    public double divide(Quantity<U> other) {
        validateArithmeticOperands(other, null, ArithmeticOperation.DIVIDE,
                "Quantity to divide by must not be null");
        return performBaseArithmetic(other, ArithmeticOperation.DIVIDE);
    }

    private void validateArithmeticOperands(Quantity<U> other, U targetUnit,
                                            ArithmeticOperation operation, String nullOtherMessage) {
        if (other == null) {
            throw new IllegalArgumentException(nullOtherMessage);
        }
        if (!unit.getClass().equals(other.unit.getClass())) {
            throw new IllegalArgumentException(crossCategoryMessage(operation));
        }
        unit.validateOperationSupport(operation.name());
        other.unit.validateOperationSupport(operation.name());
        if (Double.isNaN(other.value) || Double.isInfinite(other.value)) {
            throw new IllegalArgumentException("Value must be a finite number");
        }
        if (operation != ArithmeticOperation.DIVIDE && targetUnit == null) {
            throw new IllegalArgumentException("Target unit must not be null");
        }
    }

    private String crossCategoryMessage(ArithmeticOperation operation) {
        return switch (operation) {
            case ADD -> "Cannot add quantities of different categories";
            case SUBTRACT -> "Cannot subtract quantities of different categories";
            case DIVIDE -> "Cannot divide quantities of different categories";
        };
    }

    private double performBaseArithmetic(Quantity<U> other, ArithmeticOperation operation) {
        double left = this.valueInBaseUnit();
        double right = other.valueInBaseUnit();
        return operation.compute(left, right);
    }

    private Quantity<U> buildResult(Quantity<U> other, U targetUnit,
                                    ArithmeticOperation operation, boolean round) {
        double baseResult = performBaseArithmetic(other, operation);
        double converted = targetUnit.convertFromBaseUnit(baseResult);
        double finalValue = round ? roundTwoDecimals(converted) : converted;
        return new Quantity<>(finalValue, targetUnit);
    }

    private static double roundTwoDecimals(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Quantity<?> other = (Quantity<?>) obj;
        if (!unit.getClass().equals(other.unit.getClass())) {
            return false;
        }
        return Double.compare(this.valueInBaseUnit(), other.unit.convertToBaseUnit(other.value)) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(unit.getClass(), valueInBaseUnit());
    }

    @Override
    public String toString() {
        return "Quantity(" + value + ", " + unit.getUnitName() + ")";
    }
}
