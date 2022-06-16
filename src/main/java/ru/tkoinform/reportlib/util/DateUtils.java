package ru.tkoinform.reportlib.util;

import org.postgresql.util.PGInterval;
import ru.tkoinform.reportlib.model.type.RLColumnType;

import java.util.Date;
import java.util.GregorianCalendar;

public final class DateUtils {

    private DateUtils() {
        throw new UnsupportedOperationException();
    }

    /**
     * Преобразование PGInterval в Date
     *
     * @param interval
     * @return Date
     */
    public static Date toDate(PGInterval interval) {
        return new GregorianCalendar(
                interval.getYears(),
                interval.getMonths() - 1,
                interval.getDays(),
                interval.getHours(),
                interval.getMinutes(),
                (int) interval.getSeconds()
        ).getTime();
    }

    public static String toISO8061(Date date, RLColumnType type, FormatsHolder formatsHolder) {
        switch (type) {
            case DATE:
                return formatsHolder.getIsoOffsetDate().format(date);
            case TIME:
                return formatsHolder.getIsoOffsetTime().format(date);
            case DATETIME:
            default:
                return formatsHolder.getIsoOffsetDateTime().format(date);
        }
    }

    public static String format(Date date, RLColumnType type, boolean isoFormat, FormatsHolder formatsHolder) {
        switch (type) {
            case TIME:
                if (isoFormat) {
                    return formatsHolder.getIsoOffsetTime().format(date);
                }
                return formatsHolder.getTimeFormat().format(date);

            case DATE:
                if (isoFormat) {
                    return formatsHolder.getIsoOffsetDate().format(date);
                }
                return formatsHolder.getDateFormat().format(date);

            case DATETIME:
            default:
                if (isoFormat) {
                    return formatsHolder.getIsoOffsetDateTime().format(date);
                }
                return formatsHolder.getDateTimeFormat().format(date);
        }
    }

    /*
    @Deprecated
    public static String toISO8061(String date, FormatsHolder formatsHolder) {
        if (StringUtils.isBlank(date)) {
            return date;
        }
        String formattedDate;
        try {
            formattedDate = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(
                    ZonedDateTime.ofInstant(formatsHolder.getDateTimeFormat().parse(date).toInstant(), ZoneId.systemDefault())
            );
        } catch (ParseException e) {
            try {
                formattedDate = DateTimeFormatter.ISO_OFFSET_TIME.format(
                        ZonedDateTime.ofInstant(formatsHolder.getTimeFormat().parse(date).toInstant(), ZoneId.systemDefault())
                );
            } catch (ParseException parseException) {
                try {
                    formattedDate = DateTimeFormatter.ISO_OFFSET_DATE.format(
                            ZonedDateTime.ofInstant(formatsHolder.getDateFormat().parse(date).toInstant(), ZoneId.systemDefault())
                    );
                } catch (ParseException ignored) {
                    formattedDate = "";
                }
            }
        }
        return formattedDate;
    }
    */
}
