package ru.tkoinform.reportlib.model.type;

import lombok.Getter;
import ru.telecor.common.domain.EnumedDictionary;

@Getter
public enum RLColumnType implements EnumedDictionary {
    STRING("Строка"),
    IMAGE("Изображение"),
    INTEGER("Целочисленное"),
    FLOAT("С плавающей запятой"),
    DATE("Дата"),
    TIME("Время"),
    DATETIME("Дата и время"),
    BOOLEAN("Логическое");

    private String caption;

    RLColumnType(String caption) {
        this.caption = caption;
    }

    @Override
    public String getName() {
        return this.name();
    }

    public boolean isDateType() {
        return this.equals(DATE) || this.equals(TIME) || this.equals(DATETIME);
    }
}
