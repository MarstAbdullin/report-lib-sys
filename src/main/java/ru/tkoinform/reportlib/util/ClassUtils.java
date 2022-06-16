package ru.tkoinform.reportlib.util;

import org.postgresql.util.PGInterval;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

public final class ClassUtils {

    private ClassUtils() {
        throw new UnsupportedOperationException();
    }

    public static boolean isInteger(Class<?> clazz) {
        return clazz == Integer.class
                || clazz == Long.class
                || clazz == Short.class
                || clazz == int.class
                || clazz == long.class
                || clazz == short.class;
    }

    public static boolean isFloat(Class<?> clazz) {
        return clazz == Float.class
                || clazz == Double.class
                || clazz == float.class
                || clazz == double.class
                || clazz == BigDecimal.class;

    }

    public static boolean isBoolean(Class<?> clazz) {
        return clazz == Boolean.class || clazz == boolean.class;
    }

    public static boolean isDateTime(Class<?> clazz) {
        return clazz == Date.class || clazz == Timestamp.class;
    }

    public static boolean isDate(Class<?> clazz) {
        return clazz == java.sql.Date.class;
    }

    public static boolean isString(Class<?> clazz) {
        return clazz == String.class || clazz == UUID.class;
    }

    public static boolean isInterval(Class<?> clazz) {
        return clazz == PGInterval.class;
    }

    public static boolean isByteA(Class<?> clazz) {
        return clazz == byte[].class || clazz == Byte[].class;
    }
}
