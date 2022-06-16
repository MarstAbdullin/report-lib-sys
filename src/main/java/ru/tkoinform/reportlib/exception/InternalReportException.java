package ru.tkoinform.reportlib.exception;

import lombok.NoArgsConstructor;

/**
 * Внутреннее исключение, которое используется в случае, если что-то не учли
 */
@NoArgsConstructor
public class InternalReportException extends ReportLibException {

    public InternalReportException(String message) {
        super(message);
    }

    public InternalReportException(String message, Throwable cause) {
        super(message, cause);
    }
}
