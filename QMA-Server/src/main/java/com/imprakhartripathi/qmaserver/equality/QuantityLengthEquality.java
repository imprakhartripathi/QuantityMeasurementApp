package com.imprakhartripathi.qmaserver.equality;

public class QuantityLengthEquality {

    public static double convert(double value, LengthUnit sourceUnit, LengthUnit targetUnit) {
        return new Quantity<>(value, sourceUnit).convertTo(targetUnit).getValue();
    }

    public static boolean areEqual(double firstValue, LengthUnit firstUnit,
                                   double secondValue, LengthUnit secondUnit) {
        return new Quantity<>(firstValue, firstUnit)
                .equals(new Quantity<>(secondValue, secondUnit));
    }

    public static double demonstrateLengthConversion(double value, LengthUnit sourceUnit, LengthUnit targetUnit) {
        return convert(value, sourceUnit, targetUnit);
    }

    public static double demonstrateLengthConversion(Quantity<LengthUnit> length, LengthUnit targetUnit) {
        if (length == null) {
            throw new IllegalArgumentException("Length must not be null");
        }
        return length.convertTo(targetUnit).getValue();
    }

    public static Quantity<LengthUnit> add(Quantity<LengthUnit> first, Quantity<LengthUnit> second) {
        if (first == null || second == null) {
            throw new IllegalArgumentException("Lengths must not be null");
        }
        return first.add(second);
    }

    public static Quantity<LengthUnit> add(double firstValue, LengthUnit firstUnit,
                                           double secondValue, LengthUnit secondUnit) {
        return new Quantity<>(firstValue, firstUnit)
                .add(new Quantity<>(secondValue, secondUnit));
    }

    public static Quantity<LengthUnit> add(Quantity<LengthUnit> first, Quantity<LengthUnit> second,
                                           LengthUnit targetUnit) {
        if (first == null || second == null) {
            throw new IllegalArgumentException("Lengths must not be null");
        }
        if (targetUnit == null) {
            throw new IllegalArgumentException("Target unit must not be null");
        }
        return first.add(second, targetUnit);
    }

    public static Quantity<LengthUnit> add(double firstValue, LengthUnit firstUnit,
                                           double secondValue, LengthUnit secondUnit,
                                           LengthUnit targetUnit) {
        return new Quantity<>(firstValue, firstUnit)
                .add(new Quantity<>(secondValue, secondUnit), targetUnit);
    }

    public static void main(String[] args) {
        System.out.println("Input: Quantity(1.0, \"feet\") and Quantity(12.0, \"inches\")");
        System.out.println("Output: Equal (" + areEqual(1.0, LengthUnit.FEET, 12.0, LengthUnit.INCH) + ")");
        System.out.println("Input: Quantity(1.0, \"inch\") and Quantity(1.0, \"inch\")");
        System.out.println("Output: Equal (" + areEqual(1.0, LengthUnit.INCH, 1.0, LengthUnit.INCH) + ")");
        System.out.println("Input: Quantity(1.0, YARDS) and Quantity(3.0, FEET)");
        System.out.println("Output: Equal (" + areEqual(1.0, LengthUnit.YARDS, 3.0, LengthUnit.FEET) + ")");
        System.out.println("Input: Quantity(1.0, YARDS) and Quantity(36.0, INCHES)");
        System.out.println("Output: Equal (" + areEqual(1.0, LengthUnit.YARDS, 36.0, LengthUnit.INCH) + ")");
        System.out.println("Input: Quantity(2.0, CENTIMETERS) and Quantity(2.0, CENTIMETERS)");
        System.out.println("Output: Equal (" + areEqual(2.0, LengthUnit.CENTIMETERS, 2.0, LengthUnit.CENTIMETERS) + ")");
        System.out.println("Input: Quantity(1.0, CENTIMETERS) and Quantity(0.393701, INCHES)");
        System.out.println("Output: Equal (" + areEqual(1.0, LengthUnit.CENTIMETERS, 0.393701, LengthUnit.INCH) + ")");
        System.out.println("Input: convert(1.0, FEET, INCHES)");
        System.out.println("Output: " + convert(1.0, LengthUnit.FEET, LengthUnit.INCH));
        System.out.println("Input: convert(3.0, YARDS, FEET)");
        System.out.println("Output: " + convert(3.0, LengthUnit.YARDS, LengthUnit.FEET));
        System.out.println("Input: convert(36.0, INCHES, YARDS)");
        System.out.println("Output: " + convert(36.0, LengthUnit.INCH, LengthUnit.YARDS));
        System.out.println("Input: convert(1.0, CENTIMETERS, INCHES)");
        System.out.println("Output: " + convert(1.0, LengthUnit.CENTIMETERS, LengthUnit.INCH));
        System.out.println("Input: add(Quantity(1.0, FEET), Quantity(12.0, INCHES))");
        System.out.println("Output: " + add(new Quantity<>(1.0, LengthUnit.FEET),
                new Quantity<>(12.0, LengthUnit.INCH)));
        System.out.println("Input: add(Quantity(1.0, FEET), Quantity(12.0, INCHES), YARDS)");
        System.out.println("Output: " + add(new Quantity<>(1.0, LengthUnit.FEET),
                new Quantity<>(12.0, LengthUnit.INCH), LengthUnit.YARDS));
    }
}
