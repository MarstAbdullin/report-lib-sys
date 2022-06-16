package ru.tkoinform.reportlib.converter.integers;

import ru.tkoinform.reportlib.ReportFormat;
import ru.tkoinform.reportlib.model.RLReportColumn;
import ru.tkoinform.reportlib.parser.Any2Any;
import ru.tkoinform.reportlib.converter.AbstractAny2AnyFactory;

public class Number2IntegerFactory extends AbstractAny2AnyFactory<Number, Integer> {

    public Number2IntegerFactory(ReportFormat reportFormat, RLReportColumn column, Class<? extends Number> sourceClass, Class<Integer> destinationClass) {
        super(reportFormat, column, sourceClass, destinationClass);
    }

    @Override
    public Any2Any<Number, Integer> createParser() {
        return Number::intValue;
    }
}
