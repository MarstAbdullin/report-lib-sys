package ru.tkoinform.reportlib.builder;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import ru.tkoinform.reportlib.RLConstants;
import ru.tkoinform.reportlib.config.styling.StyleConfiguration;
import ru.tkoinform.reportlib.exception.InternalReportException;
import ru.tkoinform.reportlib.model.RLReportColumn;
import ru.tkoinform.reportlib.model.RLReportRequest;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ReportBuilderCsv extends AbstractReportBuilder {

    private CSVPrinter printer;

    public ReportBuilderCsv(RLReportRequest request, File tempFile, StyleConfiguration styleConfiguration) {
        super(request, tempFile, styleConfiguration);

        try {
            this.printer = new CSVPrinter(
                    new FileWriter(
                            tempFile,
                            Charset.forName(request.getSetting(RLConstants.RLReportRequestSettingsKeys.CSV_ENCODING, "encoding", RLConstants.Csv.ENCODING))
                    ), CSVFormat.EXCEL.builder()
                    .setDelimiter(request.getSetting(RLConstants.RLReportRequestSettingsKeys.CSV_DELIMITER, "delimiter", RLConstants.Csv.DELIMITER))
                    .setRecordSeparator(RLConstants.Csv.LINE_BREAKER)
                    .build()
            );
        } catch (Exception e) {
            throw new InternalReportException("Error on creating CSV printer", e);
        }
    }

    @Override
    public void writeTitle() {
        // Не используется
    }

    @Override
    public void writeParams() {
        // Не используется
    }

    @Override
    public void writeColumnNums() {
        List<String> columnsNames = request.getColumns().stream().map(RLReportColumn::getName).collect(Collectors.toList());

        try {
            printer.printRecord(IntStream.range(1, columnsNames.size() + 1).boxed().collect(Collectors.toList()));
            printer.flush();

        } catch (IOException e) {
            throw new InternalReportException("Ошибка во время записи номеров колонок в CSV файл");
        }
    }

    @Override
    public void writeHeader() {
        List<String> columnsNames = request.getColumns().stream().map(RLReportColumn::getName).collect(Collectors.toList());

        try {
            // Печатаем названия колонок
            printer.printRecord(columnsNames);
            printer.flush();

        } catch (IOException e) {
            throw new InternalReportException("Ошибка во время записи заголовка таблицы в CSV файл");
        }
    }

    @Override
    public void writeRow(Object[] rowValues) {
        try {
            // Строка с данными
            printer.printRecord(rowValues);
            printer.flush();

        } catch (IOException e) {
            throw new InternalReportException("Ошибка во время записи строки в CSV файл");
        }
    }

    @Override
    public void writeFooter() {

    }

    @Override
    public void complete() {
        try {
            this.printer.close(true);
        } catch (Exception e) {
            throw new InternalReportException("Ошибка при завершении записи в CSV файл");
        }
    }

    @Override
    public void writeError(String errorMessage) {

    }
}
