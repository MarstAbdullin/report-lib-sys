package ru.tkoinform.reportlib.parser.strings;

import org.postgresql.util.PGInterval;
import ru.tkoinform.reportlib.ReportFormat;
import ru.tkoinform.reportlib.model.RLReportColumn;
import ru.tkoinform.reportlib.parser.AbstractAny2Any;
import ru.tkoinform.reportlib.parser.Any2Any;
import ru.tkoinform.reportlib.util.ClassUtils;
import ru.tkoinform.reportlib.util.DateUtils;

import java.util.Date;
import java.util.UUID;

/**
 * Реализация парсера в строку для <code>RLReportColumn.STRING</code>
 * То есть здесь описываем все возможные стили отображения (преобразования в строку) реальных объектов ResultSet
 * при необходимости сборки финального объекта из строк (когда не поддерживается внутреннее преобразование)
 * (По сути это большинство форматов, HTML, CSV, Word, PDF)
 * <p>
 * На вход <code>SRC</code> подается любой объект, который мы готовы преобразовать в строку
 * <p>
 * Если тип колонки строка, то на вход в качестве SRC мы по большому счету должны принимать практически весь
 * спектр того, что пришло из базы данных
 * <p>
 * ЭТО ТО, КАК МЫ ПРЕОБРАЗУЕМ В СТРОКУ ОБЪЕКТЫ, КОТОРЫЕ ОПРЕДЕЛИЛИ САМОСТОЯТЕЛЬНО (преобразования по умолчанию)
 * По сути не имея здесь права применять тонкие настройки из RSColumn не относящиеся к строке
 */
public class String2String<SRC> extends AbstractAny2Any<SRC, String> {

    public String2String(RLReportColumn column, ReportFormat reportFormat, Class<SRC> resultSetClass) {
        super(column, reportFormat, resultSetClass);
    }

    @Override
    protected Any2Any<SRC, String> createInternalFormatter() {
        if (resultSetClass == String.class) {
            // Из базы данных пришла строка
            return value -> value == null ? "" : (String) value;

        } else if (resultSetClass == UUID.class) {
            // Из базы данных пришел UUID. Отображаем через стандартный toString
            return value -> value == null ? "" : (value).toString();

        } else if (ClassUtils.isDateTime(resultSetClass)) {
            // Из базы данных пришла дата / время. Отображаем её в формате даты времени по умолчанию
            // Для CSV формата отображаем ISO
            if (reportFormat == ReportFormat.CSV) {
                return value -> value == null ? "" : DateUtils.toISO8061((Date) value, column.getType(), formatsHolder);
            }
            return value -> value == null ? "" : formatsHolder.getDateTimeFormat().format(value);

        } else if (ClassUtils.isDate(resultSetClass)) {
            // Если из базы данных пришла дата. Отображаем её в формате даты по умолчанию
            return value -> value == null ? "" : formatsHolder.getDateFormat().format(value);

        } else if (ClassUtils.isInterval(resultSetClass)) {
            // Если из базы данных пришел интервал. Отображаем его в формате даты времени по умолчанию
            return value -> value == null ? "" : formatsHolder.getDateTimeFormat().format(DateUtils.toDate((PGInterval) value));

        } else if (ClassUtils.isBoolean(resultSetClass)) {
            // Если из базы данных пришло булевое значение
            return value -> value == null ? "" : Boolean.TRUE.equals(value) ? "1" : "0";

        } else if (ClassUtils.isInteger(resultSetClass)) {
            // Если из базы данных пришло целое число, отображаем его с делением на разряды
            return value -> value == null ? "" : formatsHolder.getIntegerFormatSpaces().format(value);

        } else if (ClassUtils.isFloat(resultSetClass)) {
            // Если из базы пришла дробь, то отображаем с делением на разряды с разделителем точкой
            return value -> value == null ? "" :  formatsHolder.getFloatFormatSpacesComma().format(value);

        } else {
            // Если что-то непонятное, то вызываем классический toString при этом пишем варнинг
            logger.warn("Unsupported input class " + (resultSetClass == null ? "null" : resultSetClass) + " in String2String implementations. Use default");
            return value -> value == null ? "" : value.toString();
        }
    }
}
