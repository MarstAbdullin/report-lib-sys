package ru.tkoinform.reportlib.parser.strings;

import ru.tkoinform.reportlib.ReportFormat;
import ru.tkoinform.reportlib.exception.ReportLibException;
import ru.tkoinform.reportlib.model.RLReportColumn;
import ru.tkoinform.reportlib.model.type.RLColumnType;
import ru.tkoinform.reportlib.parser.AbstractAny2Any;

@Deprecated
public class StringParserFactory {

    protected StringParserFactory() {
        throw new UnsupportedOperationException();
    }

    public static <R> AbstractAny2Any<R, String> createStringParser(
            RLReportColumn column,
            ReportFormat reportFormat,
            Class<R> resultSetClass
    ) {
        switch (column.getType()) {
            case STRING:
                return new String2String<R>(column, reportFormat, resultSetClass);
            case IMAGE:
                return new Image2String<R>(column, reportFormat, resultSetClass);
            case INTEGER:
                return new Integer2String<R>(column, reportFormat, resultSetClass);
            case FLOAT:
                return new Float2String<R>(column, reportFormat, resultSetClass);
            case DATE:
                return new Date2String<R>(column, reportFormat, resultSetClass);
            case TIME:
                return new Time2String<R>(column, reportFormat, resultSetClass);
            case DATETIME:
                return new DateTime2String<R>(column, reportFormat, resultSetClass);
            case BOOLEAN:
                return new Boolean2String<R>(column, reportFormat, resultSetClass);
            default:
                throw new ReportLibException(String.format("Не найден парсер типа %s->%s", column.getType(), RLColumnType.STRING));
        }
    }
}
