package ru.tkoinform.reportlib.config.styling.elements;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import ru.tkoinform.reportlib.model.RLDimension;

@SuperBuilder
@Getter
public class RLElementStyle {

    private RLDimension fontSize;
    private String fontFamily;
    private String fontColor;

    private boolean isTextBold;
    private boolean isTextItalics;
    private boolean isTextUnderline;

    private RLDimension height;
    private RLDimension minHeight;

    private String borderColor;
    private RLDimension borderWidth;

    private RLDimension marginLeft;
    private RLDimension marginTop;
    private RLDimension marginRight;
    private RLDimension marginBottom;

    private String backgroundColor;

}
