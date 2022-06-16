package ru.tkoinform.reportlib.util;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.telecor.common.domain.DateHelper;
import ru.tkoinform.reportlib.config.RLDecimalFormatSymbols;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

/**
 * Все форматеры не потокобезопасны.
 * Использовать потокобезопасные нецелесообразно, т.к. сами себе создадим узкие места в пересечении разных потоков при постороении разных отчетов
 * Поэтому вывожу класс Format за пределы статики в создание нового инстанса.
 * Создавать инстанс объекта Format логично при построении каждого отчета (внутри потока). Важно не создавать инстанс на каждой строке
 * Я выберу более простой подход - создам этот объект в абстрактном классе AbstractAny2Any, так как инициализация парсера вынесена за пределы большого цикла.
 * <p>
 * Класс вводится вместо RLConstraints.Format.
 */
@Getter
public class FormatsHolder {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public FormatsHolder() {
        logger.info("Initialized new FormatsHolder for thread '{}'", Thread.currentThread().getName());
    }

    private final DecimalFormat integerFormatSpaces = new DecimalFormat("###,###", new RLDecimalFormatSymbols());
    private final DecimalFormat floatFormatSpacesComma = new DecimalFormat("###,###.##", new RLDecimalFormatSymbols());

    private final DateFormat isoOffsetDateTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
    private final DateFormat isoOffsetDate = new SimpleDateFormat("yyyy-MM-dd");
    private final DateFormat isoOffsetTime = new SimpleDateFormat("HH:mm:ss.SSSXXX");

    // Формат дат
    private final DateFormat dateFormat = DateHelper.fullDateFormat();

    // Формат времени
    private final DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

    // Формат даты + время
    private final DateFormat dateTimeFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

}
