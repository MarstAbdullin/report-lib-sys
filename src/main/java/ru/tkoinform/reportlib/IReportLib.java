package ru.tkoinform.reportlib;

import ru.tkoinform.reportlib.exception.ReportLibException;
import ru.tkoinform.reportlib.model.RLReportRequest;

import java.io.File;

/**
 * Основной класс для построения отчетов, entrypoint
 */
public interface IReportLib {

    /**
     * Построить отчет
     *
     * @param reportRequest запрос отчета
     * @return файл отчета
     * @throws ReportLibException
     */
    File build(RLReportRequest reportRequest) throws ReportLibException;
}
