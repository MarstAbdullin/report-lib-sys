package ru.tkoinform.reportlib.converter.strings;

import ru.tkoinform.reportlib.ReportFormat;
import ru.tkoinform.reportlib.model.RLReportColumn;
import ru.tkoinform.reportlib.parser.Any2Any;
import ru.tkoinform.reportlib.converter.AbstractAny2AnyFactory;

public class Boolean2StringFactory extends AbstractAny2AnyFactory<Boolean, String> {

    public Boolean2StringFactory(ReportFormat reportFormat, RLReportColumn column, Class<? extends Boolean> sourceClass, Class<String> destinationClass) {
        super(reportFormat, column, sourceClass, destinationClass);
    }

    @Override
    public Any2Any<Boolean, String> createParser() {
        if (ReportFormat.HTML.equals(format)) {
            return (value) -> Boolean.TRUE.equals(value) ? "Да" : "Нет";
        }

        return (value) -> Boolean.TRUE.equals(value) ? "1" : "0";
    }
}
