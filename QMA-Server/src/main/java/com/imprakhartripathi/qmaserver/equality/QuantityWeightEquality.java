package com.imprakhartripathi.qmaserver.equality;

import java.util.Objects;

public class QuantityWeightEquality {

    /**
     * Immutable value object representing a weight with a unit.
     */
    public static final class QuantityWeight {
        private final double value;
        private final WeightUnit unit;

        public QuantityWeight(double value, WeightUnit unit) {
            if (unit == null) {
                throw new IllegalArgumentException("Unit must not be null");
            }
            if (Double.isNaN(value) || Double.isInfinite(value)) {
                throw new IllegalArgumentException("Value must be a finite number");
            }
            this.value = value;
            this.unit = unit;
        }

        private double valueInKilograms() {
            return unit.convertToBaseUnit(value);
        }

        public double getValue() {
            return value;
        }

        public WeightUnit getUnit() {
            return unit;
        }

        public QuantityWeight convertTo(WeightUnit targetUnit) {
            if (targetUnit == null) {
                throw new IllegalArgumentException("Target unit must not be null");
            }
            double valueInKilograms = valueInKilograms();
            double converted = targetUnit.convertFromBaseUnit(valueInKilograms);
            return new QuantityWeight(converted, targetUnit);
        }

        public QuantityWeight add(QuantityWeight other) {
            if (other == null) {
                throw new IllegalArgumentException("Weight to add must not be null");
            }
            return addToTarget(other, this.unit);
        }

        public QuantityWeight add(QuantityWeight other, WeightUnit targetUnit) {
            if (other == null) {
                throw new IllegalArgumentException("Weight to add must not be null");
            }
            if (targetUnit == null) {
                throw new IllegalArgumentException("Target unit must not be null");
            }
            return addToTarget(other, targetUnit);
        }

        private QuantityWeight addToTarget(QuantityWeight other, WeightUnit targetUnit) {
            double totalKilograms = this.valueInKilograms() + other.valueInKilograms();
            double converted = targetUnit.convertFromBaseUnit(totalKilograms);
            return new QuantityWeight(converted, targetUnit);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            QuantityWeight other = (QuantityWeight) obj;
            return Double.compare(this.valueInKilograms(), other.valueInKilograms()) == 0;
        }

        @Override
        public int hashCode() {
            return Objects.hash(valueInKilograms());
        }

        @Override
        public String toString() {
            return "QuantityWeight(" + value + ", " + unit + ")";
        }
    }

    public static double convert(double value, WeightUnit sourceUnit, WeightUnit targetUnit) {
        return new QuantityWeight(value, sourceUnit).convertTo(targetUnit).value;
    }

    public static boolean areEqual(double firstValue, WeightUnit firstUnit,
                                   double secondValue, WeightUnit secondUnit) {
        return new QuantityWeight(firstValue, firstUnit)
                .equals(new QuantityWeight(secondValue, secondUnit));
    }

    public static QuantityWeight add(QuantityWeight first, QuantityWeight second) {
        if (first == null || second == null) {
            throw new IllegalArgumentException("Weights must not be null");
        }
        return first.add(second);
    }

    public static QuantityWeight add(double firstValue, WeightUnit firstUnit,
                                     double secondValue, WeightUnit secondUnit) {
        return new QuantityWeight(firstValue, firstUnit)
                .add(new QuantityWeight(secondValue, secondUnit));
    }

    public static QuantityWeight add(QuantityWeight first, QuantityWeight second, WeightUnit targetUnit) {
        if (first == null || second == null) {
            throw new IllegalArgumentException("Weights must not be null");
        }
        if (targetUnit == null) {
            throw new IllegalArgumentException("Target unit must not be null");
        }
        return first.add(second, targetUnit);
    }

    public static QuantityWeight add(double firstValue, WeightUnit firstUnit,
                                     double secondValue, WeightUnit secondUnit,
                                     WeightUnit targetUnit) {
        return new QuantityWeight(firstValue, firstUnit)
                .add(new QuantityWeight(secondValue, secondUnit), targetUnit);
    }
}
