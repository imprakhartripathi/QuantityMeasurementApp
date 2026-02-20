package com.imprakhartripathi.qmaserver.equality;

import java.util.Objects;

public class QuantityLengthEquality {

    /**
     * Immutable value object representing a length with a unit.
     */
    public static final class QuantityLength {
        private final double value;
        private final LengthUnit unit;

        public QuantityLength(double value, LengthUnit unit) {
            if (unit == null) {
                throw new IllegalArgumentException("Unit must not be null");
            }
            if (Double.isNaN(value) || Double.isInfinite(value)) {
                throw new IllegalArgumentException("Value must be a finite number");
            }
            this.value = value;
            this.unit = unit;
        }

        private double valueInFeet() {
            return unit.convertToBaseUnit(value);
        }

        public double getValue() {
            return value;
        }

        public LengthUnit getUnit() {
            return unit;
        }

        public QuantityLength convertTo(LengthUnit targetUnit) {
            if (targetUnit == null) {
                throw new IllegalArgumentException("Target unit must not be null");
            }
            double valueInFeet = valueInFeet();
            double converted = targetUnit.convertFromBaseUnit(valueInFeet);
            return new QuantityLength(converted, targetUnit);
        }

        public QuantityLength add(QuantityLength other) {
            if (other == null) {
                throw new IllegalArgumentException("Length to add must not be null");
            }
            return addToTarget(other, this.unit);
        }

        public QuantityLength add(QuantityLength other, LengthUnit targetUnit) {
            if (other == null) {
                throw new IllegalArgumentException("Length to add must not be null");
            }
            if (targetUnit == null) {
                throw new IllegalArgumentException("Target unit must not be null");
            }
            return addToTarget(other, targetUnit);
        }

        private QuantityLength addToTarget(QuantityLength other, LengthUnit targetUnit) {
            double totalFeet = this.valueInFeet() + other.valueInFeet();
            double converted = targetUnit.convertFromBaseUnit(totalFeet);
            return new QuantityLength(converted, targetUnit);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            QuantityLength other = (QuantityLength) obj;
            return Double.compare(this.valueInFeet(), other.valueInFeet()) == 0;
        }

        @Override
        public int hashCode() {
            return Objects.hash(valueInFeet());
        }

        @Override
        public String toString() {
            return "QuantityLength(" + value + ", " + unit + ")";
        }
    }

    /**
     * Converts a value between two length units.
     */
    public static double convert(double value, LengthUnit sourceUnit, LengthUnit targetUnit) {
        return new QuantityLength(value, sourceUnit).convertTo(targetUnit).value;
    }

    public static boolean areEqual(double firstValue, LengthUnit firstUnit,
                                   double secondValue, LengthUnit secondUnit) {
        return new QuantityLength(firstValue, firstUnit)
                .equals(new QuantityLength(secondValue, secondUnit));
    }

    public static double demonstrateLengthConversion(double value, LengthUnit sourceUnit, LengthUnit targetUnit) {
        return convert(value, sourceUnit, targetUnit);
    }

    public static double demonstrateLengthConversion(QuantityLength length, LengthUnit targetUnit) {
        if (length == null) {
            throw new IllegalArgumentException("Length must not be null");
        }
        return length.convertTo(targetUnit).value;
    }

    public static QuantityLength add(QuantityLength first, QuantityLength second) {
        if (first == null || second == null) {
            throw new IllegalArgumentException("Lengths must not be null");
        }
        return first.add(second);
    }

    public static QuantityLength add(double firstValue, LengthUnit firstUnit,
                                     double secondValue, LengthUnit secondUnit) {
        return new QuantityLength(firstValue, firstUnit)
                .add(new QuantityLength(secondValue, secondUnit));
    }

    public static QuantityLength add(QuantityLength first, QuantityLength second, LengthUnit targetUnit) {
        if (first == null || second == null) {
            throw new IllegalArgumentException("Lengths must not be null");
        }
        if (targetUnit == null) {
            throw new IllegalArgumentException("Target unit must not be null");
        }
        return first.add(second, targetUnit);
    }

    public static QuantityLength add(double firstValue, LengthUnit firstUnit,
                                     double secondValue, LengthUnit secondUnit,
                                     LengthUnit targetUnit) {
        return new QuantityLength(firstValue, firstUnit)
                .add(new QuantityLength(secondValue, secondUnit), targetUnit);
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
        System.out.println("Output: " + add(new QuantityLength(1.0, LengthUnit.FEET),
                new QuantityLength(12.0, LengthUnit.INCH)));
        System.out.println("Input: add(Quantity(1.0, FEET), Quantity(12.0, INCHES), YARDS)");
        System.out.println("Output: " + add(new QuantityLength(1.0, LengthUnit.FEET),
                new QuantityLength(12.0, LengthUnit.INCH), LengthUnit.YARDS));
    }
}
