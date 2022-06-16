package ru.tkoinform.reportlib.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.telecor.common.util.ExceptionUtils;
import ru.tkoinform.reportlib.RLConstants;
import ru.tkoinform.reportlib.ReportFormat;
import ru.tkoinform.reportlib.exception.ReportLibException;
import ru.tkoinform.reportlib.model.type.ListType;
import ru.tkoinform.reportlib.util.ColumnUtils;
import ru.tkoinform.reportlib.util.SqlUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RLReportRequest {

    /**
     * ID запроса, по которому можно получить его
     * результат, отменить или выполнить другие операции с ним
     */
    private UUID id;

    /**
     * Название отчета
     */
    private String name;

    /**
     * SQL-запрос отчета
     */
    private String query;

    /**
     * Параметры отчета (фильтры)
     */
    private List<RLReportParameter> parameters = new ArrayList<>();

    /**
     * Колонки отчета
     */
    private List<RLReportColumn> columns = new ArrayList<>();

    /**
     * Формат отчета, например word или excel
     */
    private ReportFormat format;

    /**
     * Скрыть параметры (в шапке отчета)
     */
    private Boolean hideParams;

    /**
     * Показат номера колонок
     */
    private Boolean showColumnNums;

    /**
     * Параметры стиля
     * TODO: вынести в отдельный объект?
     */

    /**
     * Высота шапки таблицы
     */
    private RLDimension headerHeight;

    /**
     * Выпота ячейки таблицы
     */
    private RLDimension cellHeight;

    /**
     * Размер шрифта
     */
    private RLDimension fontSize;

    /**
     * Альбомная ориентация листа
     * (false = потртетная)
     */
    private Boolean landscape;

    /**
     * Размер листа (если настраиваемый = доступны width и height)
     * По-умолчанию - A4
     */
    private ListType size = ListType.A4;

    /**
     * Ширина листа
     */
    private RLDimension width;

    /**
     * Высота листа
     */
    private RLDimension height;

    /**
     * Настройки отчета
     */
    private Map<String, String> settings = new HashMap<>();

    /**
     * Ограничение количества строк в отчёте
     */
    private Integer limit;

    private void addParameter(RLReportParameter parameter) {
        this.parameters.add(parameter);
    }

    private void addColumn(RLReportColumn column) {
        this.columns.add(column);
    }

    /**
     * Метод перенесен из report-engine. Сортирует колонки
     * по alias из sql.
     *
     * Выдергивает все alias из запроса, сортирует колонки по ним.
     * Если какой-то колонки в sql alias нет то она пропускается.
     *
     * Сомнительная вещь, я бы предпочел уйти от нее в сторону использования поля
     * sort в RLReportColumn.
     */
    public void sortColumns() {
        try {
            String uncommented = SqlUtils.uncommented(this.query);

            String sqlLower = uncommented.toLowerCase();

            int startIndex = sqlLower.indexOf(RLConstants.Sql.SELECT) + RLConstants.Sql.SELECT.length();

            // Запросы с from при объявлении алиасов запрещены!
            int endIndex = sqlLower.indexOf("from");
            String sqlColumnsStr = sqlLower.substring(startIndex, endIndex);
            while (sqlColumnsStr.contains("(")) {
                sqlColumnsStr = ColumnUtils.removeSubstringBetweenParentheses(sqlColumnsStr);
            }
            String[] sqlColumns = sqlColumnsStr.split(",");

            List<RLReportColumn> sortedColumns = new ArrayList<>();
            for (String sqlColumn : sqlColumns) {
                String alias = null;
                if (sqlColumn.toLowerCase().contains(" as ")) {
                    alias = sqlColumn.substring(sqlColumn.indexOf(RLConstants.Sql.AS) + RLConstants.Sql.AS.length(), sqlColumn.length());
                } else {
                    alias = sqlColumn;
                }

                //removes all whitespaces and non-visible characters
                alias = alias.replaceAll("\\s+", "");
                for (RLReportColumn reportColumn : this.columns) {
                    if (reportColumn.getCode().toLowerCase().equals(alias)) {
                        sortedColumns.add(reportColumn);
                    }
                }
            }
            Comparator<RLReportColumn> reportColumnComparator = Comparator.comparing(RLReportColumn::getSort, Comparator.nullsLast(Float::compareTo));
            sortedColumns.sort(reportColumnComparator);

            this.columns = sortedColumns;

        } catch (Exception e) {
            throw new IllegalStateException(
                    String.format(
                            "Ошибка в SQL-коде отчета, невозможно сортировать колонки по алиасам: %s",
                            ExceptionUtils.getExceptionMessage(e)
                    ),
                    e
            );
        }
    }

    /**
     * Размер стороны листа
     *
     * @return RLDimension
     */
    private RLDimension getSizeHeight() {
        switch (this.size) {
            case CUSTOM:
                return this.height;

            case A5:
            case A3:
            case A4:
                return this.size.getHeight();

            default:
                throw new ReportLibException(String.format("Unknown list format: %s", this.size));
        }
    }

    /**
     * Размер стороны листа
     *
     * @return RLDimension
     */
    private RLDimension getSizeWidth() {
        switch (this.size) {
            case CUSTOM:
                return this.width;

            case A5:
            case A3:
            case A4:
                return this.size.getWidth();

            default:
                throw new ReportLibException(String.format("Unknown list format: %s", this.size));
        }
    }

    /**
     * Рассчитанная с учетом всех параметров ширина листа
     *
     * @return RLDimension
     */
    public RLDimension getComputedWidth() {
        if (Boolean.TRUE.equals(this.landscape)) {
            return getSizeHeight();
        } else {
            return getSizeWidth();
        }
    }

    /**
     * Рассчитанная с учетом всех параметров высота листа
     *
     * @return RLDimension
     */
    public RLDimension getComputedHeight() {
        if (Boolean.TRUE.equals(this.landscape)) {
            return getSizeWidth();
        } else {
            return getSizeHeight();
        }
    }

    public <T> T getSetting(String key, String commonKey, T defaultValue) {
        return filterResultSetting(key, commonKey, defaultValue);
    }

    private <T> T filterResultSetting(String key, String commonKey, T defaultValue) {
        if (this.settings.containsKey(key)) {
            if (defaultValue.getClass() == Character.class) {
                return (T) getCharacter(key);
            }

            return (T) this.settings.get(key);
        } else if (this.settings.containsKey(commonKey)) {
            if (defaultValue.getClass() == Character.class) {
                return (T) getCharacter(commonKey);
            }
            return (T) this.settings.get(commonKey);
        }

        return defaultValue;
    }

    private Character getCharacter(String key) {
        String value = this.settings.get(key);
        return value.charAt(0);
    }
}
