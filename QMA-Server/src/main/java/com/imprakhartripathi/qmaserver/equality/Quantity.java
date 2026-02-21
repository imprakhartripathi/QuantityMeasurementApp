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

    public Quantity<U> convertTo(U targetUnit) {
        if (targetUnit == null) {
            throw new IllegalArgumentException("Target unit must not be null");
        }
        double baseValue = valueInBaseUnit();
        double converted = targetUnit.convertFromBaseUnit(baseValue);
        return new Quantity<>(converted, targetUnit);
    }

    public Quantity<U> add(Quantity<U> other) {
        if (other == null) {
            throw new IllegalArgumentException("Quantity to add must not be null");
        }
        return addToTarget(other, this.unit);
    }

    public Quantity<U> add(Quantity<U> other, U targetUnit) {
        if (other == null) {
            throw new IllegalArgumentException("Quantity to add must not be null");
        }
        if (targetUnit == null) {
            throw new IllegalArgumentException("Target unit must not be null");
        }
        return addToTarget(other, targetUnit);
    }

    public Quantity<U> subtract(Quantity<U> other) {
        if (other == null) {
            throw new IllegalArgumentException("Quantity to subtract must not be null");
        }
        return subtractToTarget(other, this.unit);
    }

    public Quantity<U> subtract(Quantity<U> other, U targetUnit) {
        if (other == null) {
            throw new IllegalArgumentException("Quantity to subtract must not be null");
        }
        if (targetUnit == null) {
            throw new IllegalArgumentException("Target unit must not be null");
        }
        return subtractToTarget(other, targetUnit);
    }

    public double divide(Quantity<U> other) {
        if (other == null) {
            throw new IllegalArgumentException("Quantity to divide by must not be null");
        }
        if (!unit.getClass().equals(other.unit.getClass())) {
            throw new IllegalArgumentException("Cannot divide quantities of different categories");
        }
        double divisor = other.valueInBaseUnit();
        if (Double.compare(divisor, 0.0) == 0) {
            throw new ArithmeticException("Division by zero");
        }
        return this.valueInBaseUnit() / divisor;
    }

    private Quantity<U> addToTarget(Quantity<U> other, U targetUnit) {
        if (!unit.getClass().equals(other.unit.getClass())) {
            throw new IllegalArgumentException("Cannot add quantities of different categories");
        }
        double totalBase = this.valueInBaseUnit() + other.valueInBaseUnit();
        double converted = targetUnit.convertFromBaseUnit(totalBase);
        return new Quantity<>(converted, targetUnit);
    }

    private Quantity<U> subtractToTarget(Quantity<U> other, U targetUnit) {
        if (!unit.getClass().equals(other.unit.getClass())) {
            throw new IllegalArgumentException("Cannot subtract quantities of different categories");
        }
        double totalBase = this.valueInBaseUnit() - other.valueInBaseUnit();
        double converted = targetUnit.convertFromBaseUnit(totalBase);
        return new Quantity<>(roundTwoDecimals(converted), targetUnit);
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
