package ru.tkoinform.reportlib.converter.strings;

import ru.tkoinform.reportlib.ReportFormat;
import ru.tkoinform.reportlib.model.RLReportColumn;
import ru.tkoinform.reportlib.parser.Any2Any;
import ru.tkoinform.reportlib.converter.AbstractAny2AnyFactory;

public class ByteA2StringFactory extends AbstractAny2AnyFactory<byte[], String> {

    public ByteA2StringFactory(ReportFormat reportFormat, RLReportColumn column, Class<? extends byte[]> sourceClass, Class<String> destinationClass) {
        super(reportFormat, column, sourceClass, destinationClass);
    }

    @Override
    public Any2Any<byte[], String> createParser() {
        return (value) -> "[image]";
    }
}
