package ru.tkoinform.reportlib.converter.strings;

import ru.tkoinform.reportlib.ReportFormat;
import ru.tkoinform.reportlib.converter.AbstractAny2AnyFactory;
import ru.tkoinform.reportlib.model.RLReportColumn;
import ru.tkoinform.reportlib.parser.Any2Any;
import ru.tkoinform.reportlib.util.ClassUtils;
import ru.tkoinform.reportlib.util.DateUtils;

import java.util.Date;

public class Number2StringFactory extends AbstractAny2AnyFactory<Number, String> {

    public Number2StringFactory(ReportFormat reportFormat, RLReportColumn column, Class<? extends Number> sourceClass, Class<String> destinationClass) {
        super(reportFormat, column, sourceClass, destinationClass);
    }

    @Override
    public Any2Any<Number, String> createParser() {
        if (column.getType().isDateType()) {
            return (value) -> DateUtils.format(new Date(value.longValue()), column.getType(), ReportFormat.CSV.equals(format), formatsHolder);
        }

        if (ClassUtils.isFloat(sourceClass)) {
            return (value) -> formatsHolder.getFloatFormatSpacesComma().format(value);
        }

        return (value) -> formatsHolder.getIntegerFormatSpaces().format(value);
    }
}
