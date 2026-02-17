package com.karasu256.locatorbarplus.config.impl.reflectors;

import com.karasu256.locatorbarplus.config.impl.FieldTypes;

import java.lang.reflect.Field;
import java.util.Optional;

public interface INumericReflector {
    default Optional<FieldTypes> reflectInt(Field field) {
        Class<?> type = field.getType();
        if (type == int.class || type == Integer.class) {
            return Optional.of(FieldTypes.INT);
        }
        return Optional.empty();
    }

    default Optional<FieldTypes> reflectFloat(Field field) {
        Class<?> type = field.getType();
        if (type == float.class || type == Float.class) {
            return Optional.of(FieldTypes.FLOAT);
        }
        return Optional.empty();
    }

    default Optional<FieldTypes> reflectDouble(Field field) {
        Class<?> type = field.getType();
        if (type == double.class || type == Double.class) {
            return Optional.of(FieldTypes.DOUBLE);
        }
        return Optional.empty();
    }

    default Optional<FieldTypes> reflectLong(Field field) {
        Class<?> type = field.getType();
        if (type == long.class || type == Long.class) {
            return Optional.of(FieldTypes.LONG);
        }
        return Optional.empty();
    }
}
