package ru.tkoinform.reportlib.parser.booleans;

import ru.tkoinform.reportlib.ReportFormat;
import ru.tkoinform.reportlib.model.RLReportColumn;
import ru.tkoinform.reportlib.parser.AbstractAny2Any;
import ru.tkoinform.reportlib.parser.Any2Any;
import ru.tkoinform.reportlib.util.ClassUtils;

public class Boolean2Boolean<SRC> extends AbstractAny2Any<SRC, Boolean> {

    public Boolean2Boolean(RLReportColumn column, ReportFormat reportFormat, Class<SRC> resultSetClass) {
        super(column, reportFormat, resultSetClass);
    }

    @Override
    protected Any2Any<SRC, Boolean> createInternalFormatter() {

        if (ClassUtils.isBoolean(resultSetClass)) {
            return value -> value == null ? null :  ((Boolean) value);

        } else if (ClassUtils.isInteger(resultSetClass)) {
            return value -> value == null ? null :  ((Number) value).byteValue() == 0 ? Boolean.FALSE : Boolean.TRUE;
        }

        throwColumnCastException();
        return null;
    }
}

