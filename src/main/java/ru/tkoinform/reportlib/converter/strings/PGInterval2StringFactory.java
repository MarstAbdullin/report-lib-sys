package ru.tkoinform.reportlib.converter.strings;

import org.postgresql.util.PGInterval;
import ru.tkoinform.reportlib.ReportFormat;
import ru.tkoinform.reportlib.model.RLReportColumn;
import ru.tkoinform.reportlib.parser.Any2Any;
import ru.tkoinform.reportlib.converter.AbstractAny2AnyFactory;
import ru.tkoinform.reportlib.util.DateUtils;

public class PGInterval2StringFactory extends AbstractAny2AnyFactory<PGInterval, String> {

    public PGInterval2StringFactory(ReportFormat reportFormat, RLReportColumn column, Class<? extends PGInterval> sourceClass, Class<String> destinationClass) {
        super(reportFormat, column, sourceClass, destinationClass);
    }

    @Override
    public Any2Any<PGInterval, String> createParser() {
        return (value) -> DateUtils.format(DateUtils.toDate(value), column.getType(), ReportFormat.CSV.equals(format), formatsHolder);
    }
}
