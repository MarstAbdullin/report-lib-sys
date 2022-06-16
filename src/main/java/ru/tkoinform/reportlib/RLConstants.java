package ru.tkoinform.reportlib;

import ru.tkoinform.reportlib.model.type.MeasureType;

public class RLConstants {

    public static final class ReportParameter {
        public static final String INTERVAL_BEGIN = "_begin";
        public static final String INTERVAL_END = "_end";
    }

    public static final class Excel {
        // 65280 / 255 взято из XSSFSheet
        public static final double PIXEL_LENGTH = 65280D / 255D / MeasureType.SYMBOLS.getLengthInPixels();
        public static final int MAX_ROWS = 1048575;
        public static final int ROW_ACCESS_WINDOW_SIZE = 3;
        public static final int DEFAULT_ROW_HEIGHT = 25;

        public static final short IMAGE_HEIGHT_EXCEL = 1250;
        public static final short HEIGHT_TO_WIDTH_COEFFICIENT_EXCEL = 4;
        public static final short COEFFICIENT_EXCEL = 20;
    }

    public static final class Csv {
        public static final String DELIMITER = "|";
        public static final Character LINE_BREAKER = '\n';
        public static final String ENCODING = "UTF-8";
    }

    public static final class Html {
        public static final int MAX_ROWS_COUNT = 1000;
        public static final short IMAGE_HEIGHT_HTML = 125;
        public static final short HEIGHT_TO_WIDTH_COEFFICIENT_HTML = 2;
    }

    public static final class Sql {
        public static final String SELECT = "select";
        public static final String AS = " as ";
    }

    public static final class Color {
        public static final String BLACK = "#000000";
        public static final String WHITE = "#FFFFFF";
    }

    public static final class FontFamily {
        public static final String CALIBRI = "calibri";
    }

    public static final class RLReportRequestSettingsKeys {
        public static final String CSV_DELIMITER = "csvDelimiter";
        public static final String CSV_ENCODING = "csvEncoding";
        public static final String IMAGE_HEIGHT =  "imageHeight";
    }
}
