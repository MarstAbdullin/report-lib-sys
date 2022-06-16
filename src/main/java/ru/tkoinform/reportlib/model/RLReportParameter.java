package ru.tkoinform.reportlib.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.tkoinform.reportlib.model.type.RLParameterType;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RLReportParameter {

    /**
     * Название параметра (которое отображается пользователю)
     */
    private String name;

    /**
     * Код параметра (технический, является ключем)
     * Например: WHERE customer_id IN (:IDS)
     * Код параметра: IDS
     *
     * При выполнении вместо :IDS подставится значение из value
     */
    private String code;

    /**
     * Тип параметра
     */
    private RLParameterType type;

    /**
     * Параметр является обязательным
     */
    private Boolean required;

    /**
     * Массив ли это
     * - если true - в value лежит массив
     * - если false - в value лежит объект
     */
    private boolean array;

    /**
     * Значение, подставляемое в запрос
     */
    private Object value;

    /**
     * Отображаемое значение для шапки отчета
     */
    private String displayableValue;

    /**
     * SQL-код, который ищется в запросе отчета, и заменяется на true
     *
     * Например: WHERE customer_id IN (:IDS)
     * Код параметра: IDS
     * sql=customer_id IN (:IDS)
     *
     * Если параметр не обязательный, то в запросе вместо "customer_id IN (:IDS)" будет true
     */
    private String sql;
}
