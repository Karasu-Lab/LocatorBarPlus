package com.karasu256.locatorbarplus.config.impl.reflectors;

import com.karasu256.locatorbarplus.config.impl.FieldTypes;

import java.lang.reflect.Field;
import java.util.Optional;

public interface IStringReflector {
    default Optional<FieldTypes> reflectString(Field field) {
        if (field.getType() == String.class) {
            return Optional.of(FieldTypes.STRING);
        }
        return Optional.empty();
    }
}
