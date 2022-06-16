package ru.tkoinform.reportlib.model.type;

import lombok.Getter;
import ru.telecor.common.domain.EnumedDictionary;

@Getter
public enum RLParameterType implements EnumedDictionary {
    STRING("Строка"),
    INTEGER("Целочисленное"),
    BOOLEAN("Бинарное"),
    LONG("Целочисленное (длинное)"),
    UUID("UUID"),
    FLOAT("С плавающей запятой"),
    DATE("Дата"),
    DATE_INTERVAL("Интервал дат"),
    TIME_INTERVAL("Интервал времени"),
    DATE_TIME("Дата и время");

    private String caption;

    RLParameterType(String caption) {
        this.caption = caption;
    }

    @Override
    public String getName() {
        return this.name();
    }
}
