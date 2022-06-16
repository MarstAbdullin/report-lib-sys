package ru.tkoinform.reportlib.model.type;

import lombok.Getter;
import ru.telecor.common.domain.EnumedDictionary;

@Getter
public enum MeasureType implements EnumedDictionary {

    PX("px", "Пикселей", 1D),
    PT("pt", "Пунктов", 4 / 3D),
    PERCENT("%", "Процентов", null), // Неизвестно как перевести в пиксели
    CM("cm", "Сантиметров", 38D),
    MM("mm", "Миллиметров", 3.8D),
    IN("in", "Дюймов", 96.52D),
    SYMBOLS("em", "Символов", 14D); // При учете что размер шрифта = 14px

    private String extension;
    private String displayName;
    private Double lengthInPixels;

    MeasureType(String extension, String displayName, Double lengthInPixels) {
        this.extension = extension;
        this.displayName = displayName;
        this.lengthInPixels = lengthInPixels;
    }

    @Override
    public String getName() {
        return this.extension;
    }

    /**
     * Найти по расширению файла
     *
     * @param extension
     * @return MeasureType
     */
    public static MeasureType findByExtension(String extension) {
        for (MeasureType measureType : values()) {
            if (extension.equals(measureType.getExtension())) {
                return measureType;
            }
        }

        return null;

        /*
        if (extension.equals(PX.getExtension())) {
            return PX;
        }
        if (extension.equals(PT.getExtension())) {
            return PT;
        }
        if (extension.equals(CM.getExtension())) {
            return CM;
        }
        if (extension.equals(MM.getExtension())) {
            return MM;
        }
        if (extension.equals(IN.getExtension())) {
            return IN;
        }
        if (extension.equals(PERCENT.getExtension())) {
            return PERCENT;
        }
        if (extension.equals(SYMBOLS.getExtension())) {
            return SYMBOLS;
        }
        return null;
        */
    }
}

