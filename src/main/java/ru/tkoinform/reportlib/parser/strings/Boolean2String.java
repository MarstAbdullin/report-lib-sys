package ru.tkoinform.reportlib.parser.strings;

import ru.tkoinform.reportlib.ReportFormat;
import ru.tkoinform.reportlib.model.RLReportColumn;
import ru.tkoinform.reportlib.parser.AbstractAny2Any;
import ru.tkoinform.reportlib.parser.Any2Any;
import ru.tkoinform.reportlib.util.ClassUtils;


/**
 * Реализация парсера в строку для <code>RLReportColumn.BOOLEAN</code>
 * То есть здесь описываем все возможные стили отображения (преобразования в строку) реальных объектов ResultSet
 * при необходимости сборки финального объекта из строк (когда не поддерживается внутреннее преобразование)
 * (По сути это большинство форматов, HTML, CSV, Word, PDF)
 * <p>
 * На вход <code>SRC</code> подается любой логический объект, который мы готовы преобразовать в строку
 * <p>
 * <p>
 * Может показаться что это избыточный класс, и делает тоже самое, что <code>String2String</code> с типизацией по
 * логическому числу, однако архитектура такова, что отображать типизированный в boolean RLReportColumn мы можем иначе,
 * чем преобразование в строку по умолчанию, в том числе используя дополнительые аттрибуты самой колонки.
 * <p>
 * <p>
 * Здесь мы применяем более тонкие настройки RSColumn относящиеся к логическим значениям.
 */
public class Boolean2String<SRC> extends AbstractAny2Any<SRC, String> {


    public Boolean2String(RLReportColumn column, ReportFormat reportFormat, Class<SRC> resultSetClass) {
        super(column, reportFormat, resultSetClass);
    }

    @Override
    protected Any2Any<SRC, String> createInternalFormatter() {
        if (ClassUtils.isBoolean(resultSetClass)) {
            // Если из базы данных пришло булевое значение
            if (reportFormat.equals(ReportFormat.HTML)) {
                return value -> value == null ? "" : Boolean.TRUE.equals(value) ? "Да" : "Нет";
            }
            return value -> value == null ? "" : Boolean.TRUE.equals(value) ? "1" : "0";
        }
        throwColumnCastException();
        return null;
    }
}
