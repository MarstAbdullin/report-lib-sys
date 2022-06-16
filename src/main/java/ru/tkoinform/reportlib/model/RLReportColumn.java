package ru.tkoinform.reportlib.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import ru.tkoinform.reportlib.model.type.RLColumnType;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RLReportColumn {

    /**
     * Код (TODO: зачем нужен?)
     */
    private String code;

    /**
     * Наименование
     */
    private String name;

    /**
     * Тип
     */
    private RLColumnType type;

    /**
     * Сортировка колонок
     */
    private Float sort;

    /**
     * Количество последующих ячеек для группировки
     */
    private Integer groupedCount;

    /**
     * Название группы
     * TODO: добавить новый класс - группа колонок, и складывать колонки в нее. Группы могут быть нескольких уровней.
     */
    private String groupName;

    /**
     * Ширина
     */
    private RLDimension width;

    /**
     * Столбец является группирующим
     *
     * @return да или нет
     */
    public boolean isGrouped() {
        return StringUtils.isNotEmpty(this.groupName) && this.groupedCount != null;
    }

    /**
     * Записана ли колонка в лог
     */
    private boolean logged;
}
