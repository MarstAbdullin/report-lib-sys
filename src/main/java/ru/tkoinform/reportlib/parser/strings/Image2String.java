package ru.tkoinform.reportlib.parser.strings;

import org.apache.commons.io.IOUtils;
import ru.tkoinform.reportlib.ReportFormat;
import ru.tkoinform.reportlib.model.RLReportColumn;
import ru.tkoinform.reportlib.parser.AbstractAny2Any;
import ru.tkoinform.reportlib.parser.Any2Any;
import ru.tkoinform.reportlib.util.ClassUtils;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;

public class Image2String<SRC> extends AbstractAny2Any<SRC, String> {

    /**
     * Создание объекта. Метод сохраняет в объекте колонку, формат отчёта и класс объекта ResultSet
     * для которых создаётся реализация, после чего вызывается метод суперкласса - создать форматтеры
     *
     * @param column         Колонка
     * @param reportFormat   Формат отчета
     * @param resultSetClass Класс объекта ResultSet для которого будет работать форматтер
     */
    public Image2String(RLReportColumn column, ReportFormat reportFormat, Class<SRC> resultSetClass) {
        super(column, reportFormat, resultSetClass);
    }

    @Override
    protected Any2Any<SRC, String> createInternalFormatter() {
        if (ClassUtils.isByteA(resultSetClass)) {
            if (reportFormat == ReportFormat.CSV) {
                return value -> value == null ? "" : "[image]";
            }
            return value -> value == null ? "" : new String((byte[]) value);
        } else if (resultSetClass == Blob.class) {
            if (reportFormat == ReportFormat.CSV) {
                return value -> value == null ? "" : "[image]";
            }
            return value -> {
                try {
                    return value == null ? "" : IOUtils.toString(((Blob) value).getBinaryStream());
                } catch (IOException | SQLException e) {
                    e.printStackTrace();
                }
                return "";
            };
        }

        throwColumnCastException();
        return null;
    }
}
