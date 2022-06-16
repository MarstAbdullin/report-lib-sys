package ru.tkoinform.reportlib.util.css;

import java.util.function.Consumer;

public class CssDeclaration {

    private String style = "";

    public String asStyle() {
        return style;
    }

    public CssDeclaration onlyIf(boolean condition, Consumer<CssDeclaration> consumer) {
        if (condition) {
            consumer.accept(this);
        }
        return this;
    }

    private CssDeclaration with(String property, Object value) {
        if (value != null) {
            style += String.format("%s: %s; ", property, value);
        }
        return this;
    }

    public CssDeclaration color(Object value) {
        return with("color", value);
    }

    public CssDeclaration fontFamily(Object fontFamily) {
        return with("font-family", fontFamily);
    }

    public CssDeclaration fontSize(Object fontSize) {
        return with("font-size", fontSize);
    }

    public CssDeclaration fontWeight(Object fontWeight) {
        return with("font-weight", fontWeight);
    }

    public CssDeclaration paddingTop(Object fontWeight) {
        return with("padding-top", fontWeight);
    }

    public CssDeclaration marginLeft(Object marginLeft) {
        return with("margin-left", marginLeft);
    }

    public CssDeclaration marginBottom(Object marginLeft) {
        return with("margin-bottom", marginLeft);
    }

    public CssDeclaration minHeight(Object minHeight) {
        return with("min-height", minHeight);
    }

    public CssDeclaration height(Object height) {
        return with("height", height);
    }

    public CssDeclaration paddingBottom(Object paddingBottom) {
        return with("padding-bottom", paddingBottom);
    }

    public CssDeclaration borderWidth(Object borderWidth) {
        return with("border-width", borderWidth);
    }

    public CssDeclaration borderColor(Object borderColor) {
        return with("border-color", borderColor);
    }

    public CssDeclaration backgroundColor(Object backgroundColor) {
        return with("background-color", backgroundColor);
    }
}
