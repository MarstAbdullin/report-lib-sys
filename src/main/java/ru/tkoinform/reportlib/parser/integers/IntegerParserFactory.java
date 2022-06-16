package ru.tkoinform.reportlib.parser.integers;

import ru.tkoinform.reportlib.ReportFormat;
import ru.tkoinform.reportlib.exception.ReportLibException;
import ru.tkoinform.reportlib.model.RLReportColumn;
import ru.tkoinform.reportlib.model.type.RLColumnType;
import ru.tkoinform.reportlib.parser.AbstractAny2Any;

@Deprecated
public class IntegerParserFactory {

    protected IntegerParserFactory() {
        throw new UnsupportedOperationException();
    }

    public static <R> AbstractAny2Any<R, Long> createIntegerParser(
            RLReportColumn column,
            ReportFormat reportFormat,
            Class<R> resultSetClass
    ) {
        switch (column.getType()) {
            case STRING:
                return new String2Integer<>(column, reportFormat, resultSetClass);
            case INTEGER:
                return new Integer2Integer<>(column, reportFormat, resultSetClass);
            case FLOAT:
                return new Float2Integer<>(column, reportFormat, resultSetClass);
            default:
                throw new ReportLibException(String.format("Не найден парсер типа %s->%s", column.getType(), RLColumnType.INTEGER));
        }
    }
}
