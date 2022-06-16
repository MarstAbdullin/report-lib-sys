package ru.tkoinform.reportlib.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.tkoinform.reportlib.ReportFormat;
import ru.tkoinform.reportlib.model.RLReportColumn;
import ru.tkoinform.reportlib.parser.Any2Any;
import ru.tkoinform.reportlib.util.FormatsHolder;

public abstract class AbstractAny2AnyFactory<SRC, DST> implements Any2AnyFactory<SRC, DST> {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected ReportFormat format;
    protected RLReportColumn column;
    protected Class<? extends SRC> sourceClass;
    protected Class<DST> destinationClass;

    /**
     * Инициализирую набор форматеров при инициализации объекта, которые можно использовать в парсере
     */
    protected FormatsHolder formatsHolder = new FormatsHolder();

    public AbstractAny2AnyFactory(ReportFormat reportFormat, RLReportColumn column, Class<? extends SRC> sourceClass, Class<DST> destinationClass) {
        this.format = reportFormat;
        this.column = column;
        this.sourceClass = sourceClass;
        this.destinationClass = destinationClass;
    }

    public Any2Any<SRC, DST> createParser() {
        throw new UnsupportedOperationException("Parser is not implemented!");
    }
}
