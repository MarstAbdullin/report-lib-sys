package ru.tkoinform.reportlib.parser.floats;

import ru.tkoinform.reportlib.ReportFormat;
import ru.tkoinform.reportlib.model.RLReportColumn;
import ru.tkoinform.reportlib.parser.AbstractAny2Any;
import ru.tkoinform.reportlib.parser.Any2Any;
import ru.tkoinform.reportlib.util.ClassUtils;

public class Integer2Float<SRC> extends AbstractAny2Any<SRC, Double> {

    public Integer2Float(RLReportColumn column, ReportFormat reportFormat, Class<SRC> resultSetClass) {
        super(column, reportFormat, resultSetClass);
    }

    @Override
    protected Any2Any<SRC, Double> createInternalFormatter() {
        if (ClassUtils.isInteger(resultSetClass)) {
            return value -> value == null ? null :  ((Number) value).doubleValue();
        }

        throwColumnCastException();
        return null;
    }
}
