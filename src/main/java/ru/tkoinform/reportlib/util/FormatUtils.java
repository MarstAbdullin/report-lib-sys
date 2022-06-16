package ru.tkoinform.reportlib.util;

import ru.tkoinform.reportlib.ReportFormat;

public final class FormatUtils {

    private FormatUtils() {
        throw new UnsupportedOperationException();
    }

    public static ReportFormat byContentType(String contentType) {
        for (ReportFormat format : ReportFormat.values()) {
            if (format.getContentType().equals(contentType)) {
                return format;
            }
        }
        return null;
    }
}
