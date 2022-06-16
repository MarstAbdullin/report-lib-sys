package ru.tkoinform.reportlib.converter.floats;

import ru.tkoinform.reportlib.ReportFormat;
import ru.tkoinform.reportlib.model.RLReportColumn;
import ru.tkoinform.reportlib.parser.Any2Any;
import ru.tkoinform.reportlib.converter.AbstractAny2AnyFactory;

public class String2FloatFactory extends AbstractAny2AnyFactory<String, Double> {

    public String2FloatFactory(ReportFormat reportFormat, RLReportColumn column, Class<? extends String> sourceClass, Class<Double> destinationClass) {
        super(reportFormat, column, sourceClass, destinationClass);
    }

    @Override
    public Any2Any<String, Double> createParser() {
        return Double::parseDouble;
    }
}
