package ru.tkoinform.reportlib.converter.integers;

import ru.tkoinform.reportlib.ReportFormat;
import ru.tkoinform.reportlib.model.RLReportColumn;
import ru.tkoinform.reportlib.parser.Any2Any;
import ru.tkoinform.reportlib.converter.AbstractAny2AnyFactory;

public class Any2IntegerFactory extends AbstractAny2AnyFactory<Object, Integer> {

    public Any2IntegerFactory(ReportFormat reportFormat, RLReportColumn column, Class<?> sourceClass, Class<Integer> destinationClass) {
        super(reportFormat, column, sourceClass, destinationClass);
    }

    @Override
    public Any2Any<Object, Integer> createParser() {
        return (value) -> Integer.parseInt(value.toString());
    }
}
