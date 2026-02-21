package com.imprakhartripathi.qmaserver.equality;

public interface IMeasurable {
    double getConversionFactor();

    double convertToBaseUnit(double value);

    double convertFromBaseUnit(double baseValue);

    String getUnitName();
}
