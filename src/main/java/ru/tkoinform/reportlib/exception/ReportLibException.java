package ru.tkoinform.reportlib.exception;

import lombok.NoArgsConstructor;

/**
 * Внутреннее исключение ReportLib
 */
@NoArgsConstructor
public class ReportLibException extends RuntimeException {

    public ReportLibException(String message) {
        super(message);
    }

    public ReportLibException(String message, Throwable cause) {
        super(message, cause);
    }
}
