package ru.tkoinform.reportlib.util;

public final class ColumnUtils {

    private ColumnUtils() {
        throw new UnsupportedOperationException();
    }

    /**
     * Перенесено из GM.
     * TODO: документировать. Пока сам не разобрался что здесь.
     *
     * @param srcString
     * @return отредактированная строка
     */
    public static String removeSubstringBetweenParentheses(String srcString) {
        StringBuilder out = new StringBuilder();
        int endIndex = srcString.indexOf(")");

        // На случай если придет открывающая скобка без закрывающей
        if (endIndex < 0) {
            endIndex = srcString.length() - 1;
        }

        // Ищем открывающую скобку этой закрывающей скобки
        int startIndex = (srcString.substring(0, endIndex)).lastIndexOf("(");

        /*
        // Поиск вложенных скобок. Если они есть - сначала удаляются внутренние скобки, потом внешние
        String toRemove = srcString.substring(startIndex + 1, endIndex);
        while (toRemove.contains("(")) {
            startIndex = startIndex + 1 + toRemove.indexOf("(");
            toRemove = srcString.substring(startIndex + 1, endIndex);
        }
        */

        out.append(srcString, 0, startIndex);
        out.append("_");
        out.append(srcString, endIndex + 1, srcString.length());

        return out.toString();
    }
}
