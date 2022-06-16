package ru.tkoinform.reportlib.config.styling.elements;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public class RLParameterDictStyle extends RLElementStyle {

    // Стиль для названий параметров
    private RLElementStyle parameterName;

    // Стиль для значений параметров
    private RLElementStyle parameterValue;

}
