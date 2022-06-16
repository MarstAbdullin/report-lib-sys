package ru.tkoinform.reportlib.converter;

import ru.tkoinform.reportlib.ReportFormat;
import ru.tkoinform.reportlib.model.RLReportColumn;
import ru.tkoinform.reportlib.parser.Any2Any;

public class Object2ObjectFactory extends AbstractAny2AnyFactory<Object, Object> {

    public Object2ObjectFactory(ReportFormat reportFormat, RLReportColumn column, Class<?> sourceClass, Class<Object> destinationClass) {
        super(reportFormat, column, sourceClass, destinationClass);
    }

    @Override
    public Any2Any<Object, Object> createParser() {
        return (value) -> value;
    }
}
