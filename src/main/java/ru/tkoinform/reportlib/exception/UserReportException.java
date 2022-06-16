package ru.tkoinform.reportlib.exception;

/**
 * Внешнее исключение, которое говорит что пользователь что-то не так сделал
 * Текст данного исключения может быть обработан HTML Тегами, и в случае если
 * его поймать на фронте можно выдавать корректное сообщение пользователю
 * <p>
 * В идеале текст исключения должен быть привязан к локализации
 */
public class UserReportException extends ReportLibException {

    public UserReportException(String message) {
        super(message);
    }

    public UserReportException(String message, Throwable cause) {
        super(message, cause);
    }
}
