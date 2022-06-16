package ru.tkoinform.reportlib.builder;

/**
 * Построитель отчета
 */
public interface ReportBuilder {

    /**
     * Построить заголовок отчета (название + дата)
     */
    void writeTitle();

    /**
     * Построить блок параметров отчета
     */
    void writeParams();

    /**
     * Построить строку с номерами колонок
     */
    void writeColumnNums();

    /**
     * Построить шапку таблицы
     */
    void writeHeader();

    /**
     * Построить строку таблицы
     *
     * @param rowValues значения строки
     */
    void writeRow(Object[] rowValues);

    /**
     * Построить подвал таблицы
     */
    void writeFooter();

    /**
     * Завершить построение отчета
     */
    void complete();

    /**
     * Вывод ошибки в отчёте
     * @param errorMessage
     */
    void writeError(String errorMessage);
}
