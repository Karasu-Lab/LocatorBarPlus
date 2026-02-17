package com.karasu256.locatorbarplus.config.impl;

import com.karasu256.locatorbarplus.config.impl.reflectors.IBooleanReflector;
import com.karasu256.locatorbarplus.config.impl.reflectors.INumericReflector;
import com.karasu256.locatorbarplus.config.impl.reflectors.IStringReflector;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public abstract class AbstractReflectedFieldGetter implements IConfigFieldsAdapter, IBooleanReflector, INumericReflector, IStringReflector {
    private final Class<?> configClass;

    protected AbstractReflectedFieldGetter(Class<?> configClass) {
        this.configClass = configClass;
    }

    @Override
    public Map<String, FieldTypes> getFields() {
        Map<String, FieldTypes> fields = new LinkedHashMap<>();
        collectFields("", configClass, fields);
        return fields;
    }

    private void collectFields(String prefix, Class<?> clazz, Map<String, FieldTypes> map) {
        for (Field field : clazz.getDeclaredFields()) {
            if (Modifier.isStatic(field.getModifiers())) continue;
            if (!Modifier.isPublic(field.getModifiers())) continue;

            String name = prefix.isEmpty() ? field.getName() : prefix + "." + field.getName();
            
            getType(field).ifPresentOrElse(
                type -> map.put(name, type),
                () -> { if (isNestedConfig(field)) collectFields(name, field.getType(), map); }
            );
        }
    }

    protected Optional<FieldTypes> getType(Field field) {
        return Stream.<java.util.function.Function<Field, Optional<FieldTypes>>>of(
            this::reflectBoolean,
            this::reflectInt,
            this::reflectFloat,
            this::reflectDouble,
            this::reflectLong,
            this::reflectString
        ).map(f -> f.apply(field)).flatMap(Optional::stream).findFirst();
    }

    protected boolean isNestedConfig(Field field) {
        Class<?> type = field.getType();
        return !type.isPrimitive() && !type.getName().startsWith("java.") && !type.isEnum();
    }
}
