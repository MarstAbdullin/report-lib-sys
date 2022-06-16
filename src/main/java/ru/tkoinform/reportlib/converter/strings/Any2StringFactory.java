package ru.tkoinform.reportlib.converter.strings;

import ru.tkoinform.reportlib.ReportFormat;
import ru.tkoinform.reportlib.model.RLReportColumn;
import ru.tkoinform.reportlib.parser.Any2Any;
import ru.tkoinform.reportlib.converter.AbstractAny2AnyFactory;

public class Any2StringFactory extends AbstractAny2AnyFactory<Object, String> {

    public Any2StringFactory(ReportFormat reportFormat, RLReportColumn column, Class<?> sourceClass, Class<String> destinationClass) {
        super(reportFormat, column, sourceClass, destinationClass);
    }

    @Override
    public Any2Any<Object, String> createParser() {
        return Object::toString;
    }
}
