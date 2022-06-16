package ru.tkoinform.reportlib.converter.booleans;

import ru.tkoinform.reportlib.ReportFormat;
import ru.tkoinform.reportlib.model.RLReportColumn;
import ru.tkoinform.reportlib.parser.Any2Any;
import ru.tkoinform.reportlib.converter.AbstractAny2AnyFactory;

public class Number2BooleanFactory extends AbstractAny2AnyFactory<Number, Boolean> {

    public Number2BooleanFactory(ReportFormat reportFormat, RLReportColumn column, Class<? extends Number> sourceClass, Class<Boolean> destinationClass) {
        super(reportFormat, column, sourceClass, destinationClass);
    }

    @Override
    public Any2Any<Number, Boolean> createParser() {
        return (value) -> value.byteValue() == 0 ? Boolean.FALSE : Boolean.TRUE;
    }
}
