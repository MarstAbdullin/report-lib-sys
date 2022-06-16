package ru.tkoinform.reportlib.config.styling.elements;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public class RLTableStyle extends RLElementStyle {

    // Стиль для каждой строки
    private RLElementStyle row;

    // Дополнительный стиль для нечетных строк
    private RLElementStyle oddRow;

    // Дополнительный стиль для четных строк
    private RLElementStyle evenRow;

    // Стиль для каждого заголовка
    private RLElementStyle head;

    // Стиль для каждой ячейки
    private RLElementStyle cell;

}
