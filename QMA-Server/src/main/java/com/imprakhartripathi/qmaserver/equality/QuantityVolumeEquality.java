package com.imprakhartripathi.qmaserver.equality;

public class QuantityVolumeEquality {

    public static double convert(double value, VolumeUnit sourceUnit, VolumeUnit targetUnit) {
        return new Quantity<>(value, sourceUnit).convertTo(targetUnit).getValue();
    }

    public static boolean areEqual(double firstValue, VolumeUnit firstUnit,
                                   double secondValue, VolumeUnit secondUnit) {
        return new Quantity<>(firstValue, firstUnit)
                .equals(new Quantity<>(secondValue, secondUnit));
    }

    public static Quantity<VolumeUnit> add(Quantity<VolumeUnit> first, Quantity<VolumeUnit> second) {
        if (first == null || second == null) {
            throw new IllegalArgumentException("Volumes must not be null");
        }
        return first.add(second);
    }

    public static Quantity<VolumeUnit> add(double firstValue, VolumeUnit firstUnit,
                                           double secondValue, VolumeUnit secondUnit) {
        return new Quantity<>(firstValue, firstUnit)
                .add(new Quantity<>(secondValue, secondUnit));
    }

    public static Quantity<VolumeUnit> add(Quantity<VolumeUnit> first, Quantity<VolumeUnit> second,
                                           VolumeUnit targetUnit) {
        if (first == null || second == null) {
            throw new IllegalArgumentException("Volumes must not be null");
        }
        if (targetUnit == null) {
            throw new IllegalArgumentException("Target unit must not be null");
        }
        return first.add(second, targetUnit);
    }

    public static Quantity<VolumeUnit> add(double firstValue, VolumeUnit firstUnit,
                                           double secondValue, VolumeUnit secondUnit,
                                           VolumeUnit targetUnit) {
        return new Quantity<>(firstValue, firstUnit)
                .add(new Quantity<>(secondValue, secondUnit), targetUnit);
    }
}
