package ru.tkoinform.reportlib.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.tkoinform.reportlib.ReportFormat;
import ru.tkoinform.reportlib.exception.UserReportException;
import ru.tkoinform.reportlib.model.RLReportColumn;
import ru.tkoinform.reportlib.util.FormatsHolder;

/**
 * Ключевой интерфейс форматирования из объектов ResultSet.Any to Any
 * <p>
 * Основная задумка - отделить инициализацию и выбор конкретного режима форматирования, заданного в конкретной
 * реализации реализаций метода <code>createInternalFormatter</code> от вызова метода <code>format</code>.
 * <p>
 * То есть метод format использует уже готовый типизированный и инициализированный ПОЛЬЗОВАТЕЛЕМ конечного класса
 * режим форматирования, направляющий компилятор сразу в нужный метод без кастингов на основе того что задано в
 * <code>SRC</code> и <code>DST</code>
 * <p>
 * Этот класс трактуется как 2 уровня преобразования. RS -> FROM -> TO
 * <p>
 * Первый уровень преобразование скрытое, инкапсулированное в реализацию. Это преобразование из объекта ResultSet в Столбец.
 * RS.getObject().getClass -> Тип столбца заданного пользователем.
 * То есть класс, который предполагает соответствие типа FROM столбцу заданному пользователем, должен уметь принимать
 * на вход ВСЕ объекты, возвращаемые из ResultSet, которые поддерживает данный столбец. Если допустем пользователь задал
 * тип столбца ЧИСЛО, а в ResultSet мы видим какую-нибудь строку, то мы кидаем ошибку, что это некорректный отчет.
 * <p>
 * Второй уровень преобразования явный, и указывается при реализации конкретного билдера отчетов. Это то, какой объект
 * и в каком формате нужен при строительстве. Для большинства случаев это будет Any2String, так как в конечном итоге мы
 * отображаем строку. При этом мы имеем возможность задать режим преобразования, в зависимости от типа отчета И (ИЛИ)
 * настроек колонки, заданных пользователем. Например, мы знаем что Дату в CSV всегда нужно отображать только в формате
 * ISO, но если дата преобразуется в HTML, то мы уже ориентируемся на русскоязычный формат, при этом если к нам
 * прилетело заданное пользователем форматирование, мы ориентируемся на него.
 *
 * @param <SRC> Из чего форматируем. Важно, это параметр в реализации соответствует ЗАДАННОМУ ПОЛЬЗОВАТЕЛЕМ столбцу.
 *              На вход может прилететь любое значение ResultSet, и в реализации мы должны обеспечить такое количество
 *              типизированных конструкторов (инициализаций), которое мы считаем допустимым для преобразования в нужный
 *              тип столбца.
 * @param <DST> во что форматируем, исходя из потребностей и возможностей реализации
 */
public abstract class AbstractAny2Any<SRC, DST> {

    protected Logger logger;

    /**
     * Колонка для которой инициирован парсер. Можно использовать для более тонкой типизации
     */
    protected RLReportColumn column;

    /**
     * Формат отчета для которого инициирован парсер. Можно использовать для более тонкой типизации
     */
    protected ReportFormat reportFormat;


    /**
     * Инициализирую набор форматеров при инициализации объекта, которые можно использовать в парсере
     */
    protected FormatsHolder formatsHolder = new FormatsHolder();

    /**
     * Метод для реализации в каждом парсере
     * Должен создавать именно новый Proxy объект с конкретной реализацией метода преобразования
     * полученном на основании типа исходных данных ResultSet.getObject(), на основании типа столбца и на основании
     * формата.
     * <p>
     * <p>
     * Именно метод format этого прокси и будет вызываться много много раз, поэтому внутри метода форматирования
     * должен быть минимум условий и бесполезных действий. Особенно в релизе
     * (например логирование каждого преобразования)
     *
     * @return Proxy объект, с правильной реализацией метода <code>format</code>.
     */
    protected abstract Any2Any<SRC, DST> createInternalFormatter();

    /**
     * Класс, соответствующий SRC (реальному объекту ResultSet.getObject().getClass)
     */
    protected Class<SRC> resultSetClass;

    protected Any2Any<SRC, DST> internalFormatter;


    /**
     * Создание объекта. Метод сохраняет в объекте колонку, формат отчёта и класс объекта ResultSet
     * для которых создаётся реализация, после чего вызывается метод суперкласса - создать форматтеры
     *
     * @param column         Колонка
     * @param reportFormat   Формат отчета
     * @param resultSetClass Класс объекта ResultSet для которого будет работать форматтер
     */
    public AbstractAny2Any(RLReportColumn column, ReportFormat reportFormat, Class<SRC> resultSetClass) {
        this.column = column;
        this.reportFormat = reportFormat;
        this.resultSetClass = resultSetClass;
        logger = LoggerFactory.getLogger(getClass());
        internalFormatter = createInternalFormatter();
    }

    /**
     * Вызываем чисто форматирование через конкретный враппер
     *
     * @param value исходный объект
     * @return результат
     */
    public DST format(SRC value) {
        return internalFormatter.format(value);
    }

    protected void throwColumnCastException() {
        throw new UserReportException(
                String.format(
                        "Некорректный отчёт. Тип колонки '%s' не соответствует запросу. Ожидается '%s' запрос возвращает '%s'",
                        column.getName(),
                        column.getType().getName(),
                        resultSetClass == null ? null : resultSetClass.getSimpleName()
                )
        );
    }

    public Class<SRC> getResultSetClass() {
        return resultSetClass;
    }
}
