package ru.tkoinform.reportlib.util;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Collection;

public final class ObjectUtils {

    private ObjectUtils() {
        throw new UnsupportedOperationException();
    }

    public static boolean isArray(Object object) {
        return object instanceof Collection || (object != null && object.getClass().isArray());
    }

    public static boolean isInteger(Object object) {
        return ClassUtils.isInteger(object.getClass());
    }

    public static boolean isFloat(Object object) {
        return ClassUtils.isFloat(object.getClass());
    }

    public static boolean isBoolean(Object object) {
        return ClassUtils.isBoolean(object.getClass());
    }

    public static boolean isDateTime(Object object) {
        return ClassUtils.isDateTime(object.getClass());
    }

    public static boolean isDate(Object object) {
        return ClassUtils.isDate(object.getClass());
    }

    public static boolean isString(Object object) {
        return ClassUtils.isString(object.getClass());
    }

    public static boolean isInterval(Object object) {
        return ClassUtils.isInterval(object.getClass());
    }

    public static byte[] toByteA(Byte[] bytea) {
        return ArrayUtils.toPrimitive(bytea);
    }
}
