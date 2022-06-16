package ru.tkoinform.reportlib.util;

import org.joda.time.LocalDate;
import ru.telecor.common.domain.DateInterval;
import ru.telecor.common.domain.TimeInterval;
import ru.tkoinform.reportlib.model.RLReportRequest;
import ru.tkoinform.reportlib.model.type.RLParameterType;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public final class ParameterUtils {

    private ParameterUtils() {
        throw new UnsupportedOperationException();
    }

    public static Class<?> resolveClass(String parameterTypeString) {
        return resolveClass(RLParameterType.valueOf(parameterTypeString));
    }

    /**
     * TODO: вынести в конструктор енума RLParameterType
     *
     * @param parameterType
     * @return class
     */
    public static Class<?> resolveClass(RLParameterType parameterType) {
        switch (parameterType) {
            case STRING:
                return String.class;

            case INTEGER:
                return Integer.class;

            case LONG:
                return Long.class;

            case UUID:
                return UUID.class;

            case FLOAT:
                return Float.class;

            case DATE:
                return LocalDate.class;

            case DATE_TIME:
                return Date.class;

            case BOOLEAN:
                return Boolean.class;

            case DATE_INTERVAL:
                return DateInterval.class;

            case TIME_INTERVAL:
                return TimeInterval.class;

            default:
                throw new IllegalStateException(String.format("Unknown parameter type: %s", parameterType));

        }
    }

    public static RLParameterType resolveDataType(Object value) {
        Object instance = value;

        if (value != null && value.getClass().isArray()) {
            Object[] array = (Object[]) value;
            if (array.length > 0) {
                instance = array[0];
            }

        } else if (value instanceof Collection) {
            Collection<?> collection = (Collection<?>) value;
            if (collection.size() > 0) {
                instance = collection.iterator().next();
            }
        }

        if (instance instanceof String) {
            return RLParameterType.STRING;

        } else if (instance instanceof UUID) {
            return RLParameterType.UUID;

        } else if (instance instanceof Integer) {
            return RLParameterType.INTEGER;

        } else if (instance instanceof Long) {
            return RLParameterType.LONG;

        } else if (instance instanceof Date) {
            return RLParameterType.DATE;

        } else if (instance instanceof LocalDate) {
            return RLParameterType.DATE;
        }

        throw new IllegalStateException(String.format("Unsupported type %s", instance));
    }

    /**
     * Параметр не заполнен
     *
     * @param parameterValue
     * @return да или нет
     */
    public static boolean isEmpty(Object parameterValue) {
        if (parameterValue == null) {
            return true;

        } else if (parameterValue.getClass().isArray()) {
            return ((Object[]) parameterValue).length == 0;
        } else if (parameterValue instanceof Collection) {
            return ((Collection<?>) parameterValue).isEmpty();
        } else if (parameterValue instanceof HashMap) {
            return ((HashMap<?, ?>) parameterValue).size() < 2;
        }

        return false;
    }

    /**
     * В отчете нет ни одного заполненного параметра
     *
     * @param request
     * @return да или нет
     */
    public static boolean allParamsEmpty(RLReportRequest request) {
        return request.getParameters() == null
                || request.getParameters().size() == 0
                || request.getParameters().stream().allMatch((p) -> isEmpty(p.getValue()));
    }
}
