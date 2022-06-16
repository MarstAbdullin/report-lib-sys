package ru.tkoinform.reportlib.parser.floats;

import ru.tkoinform.reportlib.ReportFormat;
import ru.tkoinform.reportlib.model.RLReportColumn;
import ru.tkoinform.reportlib.parser.AbstractAny2Any;
import ru.tkoinform.reportlib.parser.Any2Any;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Float2Float<SRC> extends AbstractAny2Any<SRC, Double> {

    public Float2Float(RLReportColumn column, ReportFormat reportFormat, Class<SRC> resultSetClass) {
        super(column, reportFormat, resultSetClass);
    }

    @Override
    protected Any2Any<SRC, Double> createInternalFormatter() {
        // Включает в себя округление до 2 знаков потому что в строках было так
        if (BigDecimal.class == resultSetClass) {
            return value -> value == null ? null : ((BigDecimal) value).setScale(2, RoundingMode.HALF_UP).doubleValue();

        } else if (Double.class == resultSetClass) {
            return value -> value == null ? null : BigDecimal.valueOf((Double) value).setScale(2, RoundingMode.HALF_UP).doubleValue();
        } else if (Float.class == resultSetClass) {
            return value -> value == null ? null : BigDecimal.valueOf((Float) value).setScale(2, RoundingMode.HALF_UP).doubleValue();
        }

        throwColumnCastException();
        return null;
    }
}
