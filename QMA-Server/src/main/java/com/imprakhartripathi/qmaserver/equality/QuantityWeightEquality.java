package com.imprakhartripathi.qmaserver.equality;

public class QuantityWeightEquality {

    public static double convert(double value, WeightUnit sourceUnit, WeightUnit targetUnit) {
        return new Quantity<>(value, sourceUnit).convertTo(targetUnit).getValue();
    }

    public static boolean areEqual(double firstValue, WeightUnit firstUnit,
                                   double secondValue, WeightUnit secondUnit) {
        return new Quantity<>(firstValue, firstUnit)
                .equals(new Quantity<>(secondValue, secondUnit));
    }

    public static Quantity<WeightUnit> add(Quantity<WeightUnit> first, Quantity<WeightUnit> second) {
        if (first == null || second == null) {
            throw new IllegalArgumentException("Weights must not be null");
        }
        return first.add(second);
    }

    public static Quantity<WeightUnit> add(double firstValue, WeightUnit firstUnit,
                                           double secondValue, WeightUnit secondUnit) {
        return new Quantity<>(firstValue, firstUnit)
                .add(new Quantity<>(secondValue, secondUnit));
    }

    public static Quantity<WeightUnit> add(Quantity<WeightUnit> first, Quantity<WeightUnit> second,
                                           WeightUnit targetUnit) {
        if (first == null || second == null) {
            throw new IllegalArgumentException("Weights must not be null");
        }
        if (targetUnit == null) {
            throw new IllegalArgumentException("Target unit must not be null");
        }
        return first.add(second, targetUnit);
    }

    public static Quantity<WeightUnit> add(double firstValue, WeightUnit firstUnit,
                                           double secondValue, WeightUnit secondUnit,
                                           WeightUnit targetUnit) {
        return new Quantity<>(firstValue, firstUnit)
                .add(new Quantity<>(secondValue, secondUnit), targetUnit);
    }
}
