package ru.tkoinform.reportlib.converter.integers;

import ru.tkoinform.reportlib.ReportFormat;
import ru.tkoinform.reportlib.model.RLReportColumn;
import ru.tkoinform.reportlib.parser.Any2Any;
import ru.tkoinform.reportlib.converter.AbstractAny2AnyFactory;

public class String2IntegerFactory extends AbstractAny2AnyFactory<String, Integer> {

    public String2IntegerFactory(ReportFormat reportFormat, RLReportColumn column, Class<? extends String> sourceClass, Class<Integer> destinationClass) {
        super(reportFormat, column, sourceClass, destinationClass);
    }

    @Override
    public Any2Any<String, Integer> createParser() {
        return Integer::parseInt;
    }
}
