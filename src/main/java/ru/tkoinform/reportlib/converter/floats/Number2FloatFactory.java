package ru.tkoinform.reportlib.converter.floats;

import ru.tkoinform.reportlib.ReportFormat;
import ru.tkoinform.reportlib.model.RLReportColumn;
import ru.tkoinform.reportlib.parser.Any2Any;
import ru.tkoinform.reportlib.converter.AbstractAny2AnyFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Number2FloatFactory extends AbstractAny2AnyFactory<Number, Double> {

    public Number2FloatFactory(ReportFormat reportFormat, RLReportColumn column, Class<? extends Number> sourceClass, Class<Double> destinationClass) {
        super(reportFormat, column, sourceClass, destinationClass);
    }

    @Override
    public Any2Any<Number, Double> createParser() {
        // TODO: разбить на отдельные Any2Any или упростить. Думаю что округление должно быть не здесь.

        if (BigDecimal.class == sourceClass) {
            return (value) -> ((BigDecimal) value).setScale(2, RoundingMode.HALF_UP).doubleValue();

        } else if (Double.class == sourceClass) {
            return (value) -> BigDecimal.valueOf((Double) value).setScale(2, RoundingMode.HALF_UP).doubleValue();

        } else if (Float.class == sourceClass) {
            return (value) -> BigDecimal.valueOf((Float) value).setScale(2, RoundingMode.HALF_UP).doubleValue();
        }

        logger.warn("Unsupported 'Number' class " +  sourceClass);
        return Number::doubleValue;
    }
}

