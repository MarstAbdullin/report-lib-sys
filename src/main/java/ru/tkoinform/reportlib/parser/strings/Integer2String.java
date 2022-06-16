package ru.tkoinform.reportlib.parser.strings;

import ru.tkoinform.reportlib.ReportFormat;
import ru.tkoinform.reportlib.model.RLReportColumn;
import ru.tkoinform.reportlib.parser.AbstractAny2Any;
import ru.tkoinform.reportlib.parser.Any2Any;
import ru.tkoinform.reportlib.util.ClassUtils;


/**
 * Реализация парсера в строку для <code>RLReportColumn.INTEGER</code>
 * То есть здесь описываем все возможные стили отображения (преобразования в строку) реальных объектов ResultSet
 * при необходимости сборки финального объекта из строк (когда не поддерживается внутреннее преобразование)
 * (По сути это большинство форматов, HTML, CSV, Word, PDF)
 * <p>
 * На вход <code>SRC</code> подается любой целочисленный объект, который мы готовы преобразовать в строку
 * <p>
 * <p>
 * Может показаться что это избыточный класс, и делает тоже самое, что <code>String2String</code> с типизацией по
 * целочисленному числу, однако архитектура такова, что отображать типизированный в число RLReportColumn мы можем иначе,
 * чем преобразование в строку по умолчанию, в том числе используя дополнительые аттрибуты самой колонки.
 * <p>
 * <p>
 * Здесь мы применяем более тонкие настройки RSColumn относящиеся к целым числам.
 */
public class Integer2String<SRC> extends AbstractAny2Any<SRC, String> {


    public Integer2String(RLReportColumn column, ReportFormat reportFormat, Class<SRC> resultSetClass) {
        super(column, reportFormat, resultSetClass);
    }

    @Override
    protected Any2Any<SRC, String> createInternalFormatter() {
        if (ClassUtils.isInteger(resultSetClass)) {
            // Если из базы данных пришло целое число, отображаем его с делением на разряды
            return value -> value == null ? "" :  formatsHolder.getIntegerFormatSpaces().format(value);
        }

        throwColumnCastException();
        return null;
    }
}
