package ru.tkoinform.reportlib.config;

import java.text.DecimalFormatSymbols;

public class RLDecimalFormatSymbols extends DecimalFormatSymbols {

    public RLDecimalFormatSymbols() {
        super();
        this.setGroupingSeparator('\u00A0'); // non breaking space
        this.setDecimalSeparator(',');
    }

    public RLDecimalFormatSymbols(char groupingSeparator, char decimalSeparator) {
        super();
        this.setGroupingSeparator(groupingSeparator);
        this.setDecimalSeparator(decimalSeparator);
    }
}
