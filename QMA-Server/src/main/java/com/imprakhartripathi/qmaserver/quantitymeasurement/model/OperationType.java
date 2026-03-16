package com.imprakhartripathi.qmaserver.quantitymeasurement.model;

import java.util.Locale;

public enum OperationType {
    ADD,
    SUBTRACT,
    DIVIDE,
    COMPARE,
    CONVERT;

    public static OperationType from(String value) {
        return OperationType.valueOf(value.trim().toUpperCase(Locale.ROOT));
    }
}
