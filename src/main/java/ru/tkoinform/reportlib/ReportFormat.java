package ru.tkoinform.reportlib;

import lombok.Getter;
import ru.telecor.common.domain.EnumedDictionary;

import java.util.Arrays;
import java.util.List;

@Getter
public enum ReportFormat implements EnumedDictionary {
    DOC(
            "doc",
            "Документ Microsoft Word",
            "application/msword"
    ),
    DOCX(
            "docx",
            "Документ Microsoft Word (*.docx)",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
    ),
    XLS(
            "xls",
            "Документ Microsoft Excel",
            "application/vnd.ms-excel"
    ),
    XLSX(
            "xlsx",
            "Документ Microsoft Excel (*.xlsx)",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
    ),
    PPT(
            "ppt",
            "Презентация Microsoft Power Point",
            "application/vnd.ms-powerpoint"
    ),
    PPTX(
            "pptx",
            "Презентация Microsoft Power Point (*.pptx)",
            "application/vnd.openxmlformats-officedocument.presentationml.presentation"
    ),
    PDF(
            "pdf",
            "PDF",
            "application/pdf"
    ),
    HTML(
            "html",
            "HTML",
            "text/html"
    ),
    CSV(
            "csv",
            "CSV",
            "text/csv"
    );

    private String fileExtension;
    private String displayName;
    private String contentType;

    ReportFormat(String fileExtension, String displayName, String contentType) {
        this.fileExtension = fileExtension;
        this.displayName = displayName;
        this.contentType = contentType;
    }

    @Override
    public String getName() {
        return this.fileExtension;
    }

    public static final List<ReportFormat> EXCEL_FORMATS = Arrays.asList(
            ReportFormat.XLS,
            ReportFormat.XLSX
    );
}
