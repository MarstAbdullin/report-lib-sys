package ru.tkoinform.reportlib.util;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public final class MathUtils {

    private MathUtils() {
        throw new UnsupportedOperationException();
    }

    /**
     * Округлить число с плавающей запятой до count знаков после запятой
     *
     * @param value
     * @param count
     * @return округленное число
     */
    public static double round(Object value, int count) {
        if (value == null) {
            return 0D;
        }

        MathContext context = new MathContext(count, RoundingMode.HALF_UP);

        if (value instanceof BigDecimal) {
            BigDecimal result = (BigDecimal) value;
            return result.round(context).doubleValue();
        } else {
            double doubleValue = (double) value;
            return new BigDecimal(doubleValue, context).doubleValue();
        }
    }
}
