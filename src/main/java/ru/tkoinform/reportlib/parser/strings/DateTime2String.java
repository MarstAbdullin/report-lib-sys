package ru.tkoinform.reportlib.parser.strings;

import ru.tkoinform.reportlib.ReportFormat;
import ru.tkoinform.reportlib.model.RLReportColumn;
import ru.tkoinform.reportlib.parser.AbstractAny2Any;
import ru.tkoinform.reportlib.parser.Any2Any;
import ru.tkoinform.reportlib.util.ClassUtils;
import ru.tkoinform.reportlib.util.DateUtils;

import java.util.Date;

/**
 * Реализация парсера в строку для <code>RLReportColumn.DATETIME</code>
 * <p>
 * То есть здесь описываем все возможные стили отображения (преобразования в строку) реальных объектов ResultSet
 * при необходимости сборки финального объекта из строк (когда не поддерживается внутреннее преобразование)
 * (По сути это большинство форматов, HTML, CSV, Word, PDF)
 * <p>
 * На вход <code>SRC</code> подается любая дата время, которую мы готовы преобразовать в строку
 * <p>
 * <p>
 * Может показаться что это избыточный класс, и делает тоже самое, что <code>String2String</code> с типизацией по
 * дате времени, однако архитектура такова, что отображать типизированный дату время RLReportColumn мы можем иначе,
 * чем преобразование в строку по умолчанию, в том числе используя дополнительые аттрибуты самой колонки.
 * <p>
 * <p>
 * Здесь мы применяем более тонкие настройки RSColumn относящиеся к дате времени.
 */
public class DateTime2String<SRC> extends AbstractAny2Any<SRC, String> {


    public DateTime2String(RLReportColumn column, ReportFormat reportFormat, Class<SRC> resultSetClass) {
        super(column, reportFormat, resultSetClass);
    }

    @Override
    protected Any2Any<SRC, String> createInternalFormatter() {
        if (ClassUtils.isDateTime(resultSetClass)) {
            // Если из базы данных пришла дата или ДатаВремя. Отображаем её в формате даты по умолчанию
            if (reportFormat == ReportFormat.CSV) {
                return value -> value == null ? "" : DateUtils.toISO8061((Date) value, column.getType(), formatsHolder);
            }
            return value -> value == null ? "" : formatsHolder.getDateTimeFormat().format(value);
        }
        throwColumnCastException();
        return null;
    }
}
