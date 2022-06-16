package ru.tkoinform.reportlib.builder;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.tkoinform.reportlib.RLConstants;
import ru.tkoinform.reportlib.config.styling.StyleConfiguration;
import ru.tkoinform.reportlib.exception.ReportLibException;
import ru.tkoinform.reportlib.model.RLReportRequest;

import java.io.File;

public class ReportBuilderFactory {

    protected ReportBuilderFactory() {
        throw new UnsupportedOperationException();
    }

    public static ReportBuilder createReportBuilder(RLReportRequest request, File tempFile, StyleConfiguration styleConfiguration) {
        switch (request.getFormat()) {
            case XLS:
                return new ReportBuilderExcel(
                        request,
                        tempFile,
                        styleConfiguration, new HSSFWorkbook()
                );

            case XLSX:
                // new XSSFWorkbook(); // Не стримовый
                return new ReportBuilderExcel(
                        request,
                        tempFile,
                        styleConfiguration,
                        new SXSSFWorkbook((XSSFWorkbook) null, RLConstants.Excel.ROW_ACCESS_WINDOW_SIZE, false, true)
                );

            case HTML:
                return new ReportBuilderHtml(request, tempFile, styleConfiguration);

            case CSV:
                return new ReportBuilderCsv(request, tempFile, styleConfiguration);

            case DOC:
            case DOCX:
            case PPT:
            case PPTX:
            case PDF:
            default:
                throw new ReportLibException(String.format("Формат %s не поддерживается!", request.getFormat()));
        }
    }
}
