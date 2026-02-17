package com.karasu256.locatorbarplus.config.impl.reflectors;

import com.karasu256.locatorbarplus.config.impl.FieldTypes;

import java.lang.reflect.Field;
import java.util.Optional;

public interface IBooleanReflector {
    default Optional<FieldTypes> reflectBoolean(Field field) {
        Class<?> type = field.getType();
        if (type == boolean.class || type == Boolean.class) {
            return Optional.of(FieldTypes.BOOL);
        }
        return Optional.empty();
    }
}
