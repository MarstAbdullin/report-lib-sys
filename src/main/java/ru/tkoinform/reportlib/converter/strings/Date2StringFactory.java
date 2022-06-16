package ru.tkoinform.reportlib.converter.strings;

import ru.tkoinform.reportlib.ReportFormat;
import ru.tkoinform.reportlib.model.RLReportColumn;
import ru.tkoinform.reportlib.parser.Any2Any;
import ru.tkoinform.reportlib.converter.AbstractAny2AnyFactory;
import ru.tkoinform.reportlib.util.DateUtils;

import java.util.Date;

public class Date2StringFactory extends AbstractAny2AnyFactory<Date, String> {

    public Date2StringFactory(ReportFormat reportFormat, RLReportColumn column, Class<? extends Date> sourceClass, Class<String> destinationClass) {
        super(reportFormat, column, sourceClass, destinationClass);
    }

    @Override
    public Any2Any<Date, String> createParser() {
        // TODO: разбить на отдельные Any2Any
        return (value) -> DateUtils.format(value, column.getType(), ReportFormat.CSV.equals(format), formatsHolder);
    }
}
