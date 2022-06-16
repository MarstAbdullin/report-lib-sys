package ru.tkoinform.reportlib.parser.strings;

import ru.tkoinform.reportlib.ReportFormat;
import ru.tkoinform.reportlib.model.RLReportColumn;
import ru.tkoinform.reportlib.parser.AbstractAny2Any;
import ru.tkoinform.reportlib.parser.Any2Any;
import ru.tkoinform.reportlib.util.ClassUtils;

/**
 * Реализация парсера в строку для <code>RLReportColumn.FLOAT</code>
 * То есть здесь описываем все возможные стили отображения (преобразования в строку) реальных объектов ResultSet
 * при необходимости сборки финального объекта из строк (когда не поддерживается внутреннее преобразование)
 * (По сути это большинство форматов, HTML, CSV, Word, PDF)
 * <p>
 * На вход <code>SRC</code> подается любой дробный объект, который мы готовы преобразовать в строку
 * <p>
 * <p>
 * Может показаться что это избыточный класс, и делает тоже самое, что <code>String2String</code> с типизацией по
 * дроби числу, однако архитектура такова, что отображать типизированный в дробь RLReportColumn мы можем иначе,
 * чем преобразование в строку по умолчанию, в том числе используя дополнительые аттрибуты самой колонки
 *
 * Здесь мы применяем более тонкие настройки RSColumn относящиеся к дробям.
 */
public class Float2String<SRC> extends AbstractAny2Any<SRC, String> {


    public Float2String(RLReportColumn column, ReportFormat reportFormat, Class<SRC> resultSetClass) {
        super(column, reportFormat, resultSetClass);
    }

    @Override
    protected Any2Any<SRC, String> createInternalFormatter() {
        if (ClassUtils.isFloat(resultSetClass)) {
            // Если из базы данных пришло число c плавающей запятой
            return value -> value == null ? "" :  formatsHolder.getFloatFormatSpacesComma().format(value);
        }

        throwColumnCastException();
        return null;
    }
}
