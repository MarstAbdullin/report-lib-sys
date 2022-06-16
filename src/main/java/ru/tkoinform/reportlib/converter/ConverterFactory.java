package ru.tkoinform.reportlib.converter;

import org.apache.commons.lang3.tuple.Pair;
import org.postgresql.util.PGInterval;
import ru.telecor.common.util.ExceptionUtils;
import ru.tkoinform.reportlib.ReportFormat;
import ru.tkoinform.reportlib.exception.UserReportException;
import ru.tkoinform.reportlib.model.RLReportColumn;
import ru.tkoinform.reportlib.parser.Any2Any;
import ru.tkoinform.reportlib.converter.booleans.Number2BooleanFactory;
import ru.tkoinform.reportlib.converter.byteas.Blob2ByteAFactory;
import ru.tkoinform.reportlib.converter.floats.Number2FloatFactory;
import ru.tkoinform.reportlib.converter.floats.String2FloatFactory;
import ru.tkoinform.reportlib.converter.integers.Number2IntegerFactory;
import ru.tkoinform.reportlib.converter.integers.String2IntegerFactory;
import ru.tkoinform.reportlib.converter.strings.Blob2StringFactory;
import ru.tkoinform.reportlib.converter.strings.Boolean2StringFactory;
import ru.tkoinform.reportlib.converter.strings.ByteA2StringFactory;
import ru.tkoinform.reportlib.converter.strings.Date2StringFactory;
import ru.tkoinform.reportlib.converter.strings.Number2StringFactory;
import ru.tkoinform.reportlib.converter.strings.PGInterval2StringFactory;

import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ConverterFactory {

    /**
     * Описывает все варианты преобразований данных какие поддерживает отчетная система.
     */
    private static final Map<Pair<Class<?>, Class<?>>, Class<? extends AbstractAny2AnyFactory<?, ?>>> PARSERS_FACTORIES_MAP
            = new HashMap<Pair<Class<?>, Class<?>>, Class<? extends AbstractAny2AnyFactory<?, ?>>>() {{

        // To string
        put(Pair.of(Integer.class, String.class), Number2StringFactory.class);
        put(Pair.of(Long.class, String.class), Number2StringFactory.class);
        put(Pair.of(Short.class, String.class), Number2StringFactory.class);
        put(Pair.of(Float.class, String.class), Number2StringFactory.class);
        put(Pair.of(Double.class, String.class), Number2StringFactory.class);
        put(Pair.of(BigDecimal.class, String.class), Number2StringFactory.class);
        put(Pair.of(Date.class, String.class), Date2StringFactory.class);
        put(Pair.of(java.sql.Date.class, String.class), Date2StringFactory.class);
        put(Pair.of(Timestamp.class, String.class), Date2StringFactory.class);
        put(Pair.of(PGInterval.class, String.class), PGInterval2StringFactory.class);
        put(Pair.of(Boolean.class, String.class), Boolean2StringFactory.class);
        put(Pair.of(byte[].class, String.class), ByteA2StringFactory.class);
        put(Pair.of(Blob.class, String.class), Blob2StringFactory.class);

        // To integer
        //put(Pair.of(Integer.class, Integer.class), Number2IntegerFactory.class); // Этот не нужен
        put(Pair.of(Long.class, Integer.class), Number2IntegerFactory.class);
        put(Pair.of(Short.class, Integer.class), Number2IntegerFactory.class);
        put(Pair.of(Float.class, Integer.class), Number2IntegerFactory.class);
        put(Pair.of(Double.class, Integer.class), Number2IntegerFactory.class);
        put(Pair.of(BigDecimal.class, Integer.class), Number2IntegerFactory.class);
        put(Pair.of(String.class, Integer.class), String2IntegerFactory.class);

        // To double
        put(Pair.of(Integer.class, Double.class), Number2FloatFactory.class);
        put(Pair.of(Long.class, Double.class), Number2FloatFactory.class);
        put(Pair.of(Short.class, Double.class), Number2FloatFactory.class);
        put(Pair.of(Float.class, Double.class), Number2FloatFactory.class);
        put(Pair.of(Double.class, Double.class), Number2FloatFactory.class); // Этот оставляем т.к. он обрезает знаки после запятой
        put(Pair.of(BigDecimal.class, Double.class), Number2FloatFactory.class);
        put(Pair.of(String.class, Double.class), String2FloatFactory.class);

        // To byte[]
        put(Pair.of(Blob.class, byte[].class), Blob2ByteAFactory.class);

        // To boolean
        put(Pair.of(Integer.class, Boolean.class), Number2BooleanFactory.class);
        put(Pair.of(Long.class, Boolean.class), Number2BooleanFactory.class);
        put(Pair.of(Short.class, Boolean.class), Number2BooleanFactory.class);
    }};

    protected ConverterFactory() {
        throw new UnsupportedOperationException();
    }

    public static <SRC, DST> Any2Any<SRC, DST> createParser(Class<SRC> srcClass,
                                                            Class<DST> dstClass,
                                                            ReportFormat reportFormat,
                                                            RLReportColumn column
    ) {
        Pair<Class<SRC>, Class<DST>> key = Pair.of(srcClass, dstClass);
        Class<? extends AbstractAny2AnyFactory<SRC, DST>> parserFactoryClass = (Class<? extends AbstractAny2AnyFactory<SRC, DST>>) PARSERS_FACTORIES_MAP.get(key);

        if (parserFactoryClass == null) {
            if (srcClass == dstClass) {
                // Если классы совпадают то никаких преобразований не нужно
                return (value) -> (DST) value;

            } else {
                throw new UserReportException(
                        String.format(
                                "Некорректный отчёт. Тип колонки '%s' не соответствует запросу. Ожидается '%s' запрос возвращает '%s'",
                                column.getName(),
                                column.getType().getName(),
                                srcClass == null ? null : srcClass.getSimpleName()
                        )
                );
            }
        }

        try {
            return parserFactoryClass
                    .getConstructor(
                            ReportFormat.class,
                            RLReportColumn.class,
                            Class.class,
                            Class.class
                    ).newInstance(
                            reportFormat,
                            column,
                            srcClass,
                            dstClass
                    )
                    .createParser();

        } catch (Exception e) {
            throw new UserReportException(
                    String.format(
                            "Ошибка при инициализации парсера %s->%s: %s",
                            srcClass.getSimpleName(),
                            dstClass.getSimpleName(),
                            ExceptionUtils.getNotNullErrorMessage(e)
                    ),
                    e
            );
        }
    }
}
