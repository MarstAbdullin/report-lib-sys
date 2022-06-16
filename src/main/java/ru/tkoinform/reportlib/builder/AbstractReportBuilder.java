package ru.tkoinform.reportlib.builder;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.tkoinform.reportlib.config.styling.StyleConfiguration;
import ru.tkoinform.reportlib.model.RLReportRequest;

import java.io.File;

/**
 * Абстрактный класс, от которого должны наследоваться все
 * билдеры отчетов. Содержит конструктор с необходимыми параметрами.
 */
@Getter
public abstract class AbstractReportBuilder implements ReportBuilder {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected RLReportRequest request;
    protected File tempFile;
    protected StyleConfiguration styleConfiguration;

    /**
     * Конструктор
     *
     * @param request запрос отчета
     * @param tempFile временный файл, в который нужно писать отчет
     * @param styleConfiguration макет отчета по которому формируется внешний вид отчета
     */
    public AbstractReportBuilder(RLReportRequest request, File tempFile, StyleConfiguration styleConfiguration) {
        this.request = request;
        this.tempFile = tempFile;
        this.styleConfiguration = styleConfiguration;

        request.sortColumns();
    }
}
