package ru.tkoinform.reportlib.converter.strings;

import ru.tkoinform.reportlib.ReportFormat;
import ru.tkoinform.reportlib.converter.AbstractAny2AnyFactory;
import ru.tkoinform.reportlib.model.RLReportColumn;
import ru.tkoinform.reportlib.parser.Any2Any;

import java.sql.Blob;

public class Blob2StringFactory extends AbstractAny2AnyFactory<Blob, String> {

    public Blob2StringFactory(ReportFormat reportFormat, RLReportColumn column, Class<? extends Blob> sourceClass, Class<String> destinationClass) {
        super(reportFormat, column, sourceClass, destinationClass);
    }

    @Override
    public Any2Any<Blob, String> createParser() {
        return (value) -> "[image]";
    }
}
