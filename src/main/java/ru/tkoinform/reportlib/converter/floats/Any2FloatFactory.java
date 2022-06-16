package ru.tkoinform.reportlib.converter.floats;

import ru.tkoinform.reportlib.ReportFormat;
import ru.tkoinform.reportlib.model.RLReportColumn;
import ru.tkoinform.reportlib.parser.Any2Any;
import ru.tkoinform.reportlib.converter.AbstractAny2AnyFactory;

public class Any2FloatFactory extends AbstractAny2AnyFactory<Object, Double> {

    public Any2FloatFactory(ReportFormat reportFormat, RLReportColumn column, Class<?> sourceClass, Class<Double> destinationClass) {
        super(reportFormat, column, sourceClass, destinationClass);
    }

    @Override
    public Any2Any<Object, Double> createParser() {
        return (value) -> Double.parseDouble(value.toString());
    }
}
