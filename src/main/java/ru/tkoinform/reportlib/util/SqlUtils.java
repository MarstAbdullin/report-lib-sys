package ru.tkoinform.reportlib.util;

import java.util.regex.Pattern;

public final class SqlUtils {

    private static final Pattern COMMENT_PATTERN =
            Pattern.compile("/\\*.*?\\*/|--.*?$", Pattern.DOTALL | Pattern.MULTILINE);

    private SqlUtils() {
        throw new UnsupportedOperationException();
    }

    public static String uncommented(String sqlWithComments) {
        return COMMENT_PATTERN.matcher(sqlWithComments).replaceAll("");
    }

}
