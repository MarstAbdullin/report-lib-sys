package ru.tkoinform.reportlib.parser;

import ru.tkoinform.reportlib.ReportFormat;
import ru.tkoinform.reportlib.exception.ReportLibException;
import ru.tkoinform.reportlib.model.RLReportColumn;
import ru.tkoinform.reportlib.parser.booleans.Boolean2Boolean;
import ru.tkoinform.reportlib.parser.byteAs.Image2ByteA;
import ru.tkoinform.reportlib.parser.floats.Float2Float;
import ru.tkoinform.reportlib.parser.integers.Integer2Integer;
import ru.tkoinform.reportlib.parser.strings.Boolean2String;
import ru.tkoinform.reportlib.parser.strings.Date2String;
import ru.tkoinform.reportlib.parser.strings.DateTime2String;
import ru.tkoinform.reportlib.parser.strings.Float2String;
import ru.tkoinform.reportlib.parser.strings.Image2String;
import ru.tkoinform.reportlib.parser.strings.Integer2String;
import ru.tkoinform.reportlib.parser.strings.String2String;
import ru.tkoinform.reportlib.parser.strings.Time2String;

public class ParserFactory {

    protected ParserFactory() {
        throw new UnsupportedOperationException();
    }

    public static <SRC> Any2Any<SRC, ?> createStringParser(
            RLReportColumn column,
            ReportFormat reportFormat,
            Class<SRC> resultSetClass
    ) {
        switch (column.getType()) {
            case STRING:
                return new String2String<SRC>(column, reportFormat, resultSetClass).internalFormatter;

            case IMAGE:
                if (ReportFormat.EXCEL_FORMATS.contains(reportFormat) || ReportFormat.HTML.equals(reportFormat)) {
                    return new Image2ByteA<SRC>(column, reportFormat, resultSetClass).internalFormatter;
                } else {
                    return new Image2String<SRC>(column, reportFormat, resultSetClass).internalFormatter;
                }


            case INTEGER:
                if (ReportFormat.EXCEL_FORMATS.contains(reportFormat) || ReportFormat.HTML.equals(reportFormat)) {
                    return new Integer2Integer<SRC>(column, reportFormat, resultSetClass).internalFormatter;
                } else {
                    return new Integer2String<SRC>(column, reportFormat, resultSetClass).internalFormatter;
                }

            case FLOAT:
                if (ReportFormat.EXCEL_FORMATS.contains(reportFormat) || ReportFormat.HTML.equals(reportFormat)) {
                    return new Float2Float<SRC>(column, reportFormat, resultSetClass).internalFormatter;
                } else {
                    return new Float2String<SRC>(column, reportFormat, resultSetClass).internalFormatter;
                }

            case DATE:
                return new Date2String<SRC>(column, reportFormat, resultSetClass).internalFormatter;

            case TIME:
                return new Time2String<SRC>(column, reportFormat, resultSetClass).internalFormatter;

            case DATETIME:
                return new DateTime2String<SRC>(column, reportFormat, resultSetClass).internalFormatter;

            case BOOLEAN:
                if (ReportFormat.EXCEL_FORMATS.contains(reportFormat)) {
                    return new Boolean2Boolean<SRC>(column, reportFormat, resultSetClass).internalFormatter;
                } else {
                    return new Boolean2String<SRC>(column, reportFormat, resultSetClass).internalFormatter;
                }

            default:
                throw new ReportLibException(String.format("Не найден парсер типа %s", column.getType()));
        }
    }
}
