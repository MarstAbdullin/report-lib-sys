package ru.tkoinform.reportlib.util.css;

public class CssStyleSheet {

    private String css = "";

    public CssStyleSheet addRule(String selector, CssDeclaration cssDeclaration) {
        return this.addRule(selector, cssDeclaration.asStyle());
    }

    private CssStyleSheet addRule(String selector, String style) {
        this.css += String.format("%s { %s} ", selector, style);
        return this;
    }

    public String asCss() {
        return this.css;
    }

}
