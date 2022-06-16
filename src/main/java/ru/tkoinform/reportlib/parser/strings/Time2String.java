package ru.tkoinform.reportlib.parser.strings;

import org.postgresql.util.PGInterval;
import ru.tkoinform.reportlib.ReportFormat;
import ru.tkoinform.reportlib.model.RLReportColumn;
import ru.tkoinform.reportlib.parser.AbstractAny2Any;
import ru.tkoinform.reportlib.parser.Any2Any;
import ru.tkoinform.reportlib.util.ClassUtils;
import ru.tkoinform.reportlib.util.DateUtils;

import java.util.Date;

/**
 * Реализация парсера в строку для <code>RLReportColumn.TIME</code>
 * <p>
 * То есть здесь описываем все возможные стили отображения (преобразования в строку) реальных объектов ResultSet
 * при необходимости сборки финального объекта из строк (когда не поддерживается внутреннее преобразование)
 * (По сути это большинство форматов, HTML, CSV, Word, PDF)
 * <p>
 * На вход <code>SRC</code> подается любое время, которую мы готовы преобразовать в строку
 * <p>
 * <p>
 * Может показаться что это избыточный класс, и делает тоже самое, что <code>String2String</code> с типизацией по
 * дате времени, однако архитектура такова, что отображать типизированное во время RLReportColumn мы можем иначе,
 * чем преобразование в строку по умолчанию, в том числе используя дополнительые аттрибуты самой колонки.
 * <p>
 * <p>
 * Здесь мы применяем более тонкие настройки RSColumn относящиеся к временам.
 */
public class Time2String<SRC> extends AbstractAny2Any<SRC, String> {

    public Time2String(RLReportColumn column, ReportFormat reportFormat, Class<SRC> resultSetClass) {
        super(column, reportFormat, resultSetClass);
    }

    @Override
    protected Any2Any<SRC, String> createInternalFormatter() {
        if (ClassUtils.isDateTime(resultSetClass)) {
            // ДатаВремя
            if (reportFormat == ReportFormat.CSV) {
                return value -> value == null ? "" : DateUtils.toISO8061((Date) value, column.getType(), formatsHolder);
            }
            return value -> value == null ? "" : formatsHolder.getTimeFormat().format(value);

        } else if (ClassUtils.isInterval(resultSetClass)) {
            // todo (как где-то упоминалось, имеет смысл для интервала сделать колонку
            // временно разрешаем подавать интервал в колонку с типом TIME
            if (reportFormat == ReportFormat.CSV) {
                return value -> value == null ? "" : DateUtils.toISO8061(DateUtils.toDate((PGInterval) value), column.getType(), formatsHolder);
            }
            return value -> value == null ? "" : formatsHolder.getTimeFormat().format(DateUtils.toDate((PGInterval) value));

        } else if (ClassUtils.isInteger(resultSetClass)) {
            // Число
            if (reportFormat == ReportFormat.CSV) {
                return value -> value == null ? "" : DateUtils.toISO8061(new Date((Long) value), column.getType(), formatsHolder);
            }
            return value -> value == null ? "" : formatsHolder.getTimeFormat().format(new Date((Long) value));
        }

        throwColumnCastException();
        return null;





    }
}

