package ru.tkoinform.reportlib.parser.integers;

import ru.tkoinform.reportlib.ReportFormat;
import ru.tkoinform.reportlib.model.RLReportColumn;
import ru.tkoinform.reportlib.parser.AbstractAny2Any;
import ru.tkoinform.reportlib.parser.Any2Any;
import ru.tkoinform.reportlib.util.ClassUtils;

public class Float2Integer<SRC> extends AbstractAny2Any<SRC, Long> {

    public Float2Integer(RLReportColumn column, ReportFormat reportFormat, Class<SRC> resultSetClass) {
        super(column, reportFormat, resultSetClass);
    }

    @Override
    protected Any2Any<SRC, Long> createInternalFormatter() {
        if (ClassUtils.isFloat(resultSetClass)) {
            return value -> value == null ? null :  ((Number) value).longValue();
        }

        throwColumnCastException();
        return null;
    }
}
