package ru.tkoinform.reportlib.model.type;

import lombok.Getter;
import ru.telecor.common.domain.EnumedDictionary;
import ru.tkoinform.reportlib.model.RLDimension;

@Getter
public enum ListType implements EnumedDictionary {

    A5("a5", "A5", new RLDimension(210D, MeasureType.MM), new RLDimension(128D, MeasureType.MM)), // 210 x 148 mm
    A4("a4", "A4", new RLDimension(297D, MeasureType.MM), new RLDimension(210D, MeasureType.MM)), // 297 x 210 mm
    A3("a3", "A3", new RLDimension(420D, MeasureType.MM), new RLDimension(297D, MeasureType.MM)), // 420 x 297 mm
    CUSTOM("custom", "Настраиваемый");

    private String name;
    private String displayName;
    private RLDimension width;
    private RLDimension height;

    ListType(String name, String displayName) {
        this.name = name;
        this.displayName = displayName;
    }

    ListType(String name, String displayName,  RLDimension height, RLDimension width) {
        this.name = name;
        this.displayName = displayName;
        this.height = height;
        this.width = width;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
