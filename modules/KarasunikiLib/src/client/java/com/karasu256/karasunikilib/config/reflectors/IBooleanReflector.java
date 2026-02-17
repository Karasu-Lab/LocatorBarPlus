package com.karasu256.karasunikilib.config.reflectors;

import com.karasu256.karasunikilib.config.FieldTypes;

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
