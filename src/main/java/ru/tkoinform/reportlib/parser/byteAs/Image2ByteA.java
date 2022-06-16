package ru.tkoinform.reportlib.parser.byteAs;

import org.apache.commons.lang3.ArrayUtils;
import ru.tkoinform.reportlib.ReportFormat;
import ru.tkoinform.reportlib.exception.ReportLibException;
import ru.tkoinform.reportlib.model.RLReportColumn;
import ru.tkoinform.reportlib.parser.AbstractAny2Any;
import ru.tkoinform.reportlib.parser.Any2Any;
import ru.tkoinform.reportlib.util.ClassUtils;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;

public class Image2ByteA<SRC> extends AbstractAny2Any<SRC, Byte[]> {

    /**
     * Создание объекта. Метод сохраняет в объекте колонку, формат отчёта и класс объекта ResultSet
     * для которых создаётся реализация, после чего вызывается метод суперкласса - создать форматтеры
     *
     * @param column         Колонка
     * @param reportFormat   Формат отчета
     * @param resultSetClass Класс объекта ResultSet для которого будет работать форматтер
     */
    public Image2ByteA(RLReportColumn column, ReportFormat reportFormat, Class<SRC> resultSetClass) {
        super(column, reportFormat, resultSetClass);
    }

    @Override
    protected Any2Any<SRC, Byte[]> createInternalFormatter() {
        if (ClassUtils.isByteA(resultSetClass)) {
            return value -> value == null ? new Byte[]{} : ArrayUtils.toObject((byte[]) value);

        } else if (resultSetClass == Blob.class) {
            return value -> {
                try {
                    return value == null ? new Byte[]{} : ArrayUtils.toObject(((Blob) value).getBinaryStream().readAllBytes());
                } catch (IOException | SQLException e) {
                    throw new ReportLibException("Error on converting Blob to byte[]", e);
                }
            };
        }

        throwColumnCastException();
        return null;
    }
}
