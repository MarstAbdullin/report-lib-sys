package ru.tkoinform.reportlib.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.tkoinform.reportlib.model.type.MeasureType;

import java.util.function.BiFunction;

/**
 * Измерение какой-либо величины
 * Содержит количество и тип (чего - сантиметров, миллиметров, типографических пунктов и т.д.)
 */
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
public class RLDimension {

    private Double number;

    private MeasureType type;

    public static RLDimension valued(Number number, MeasureType type) {
        return new RLDimension(number.doubleValue(), type);
    }

    public RLDimension minus(RLDimension other) {
        return apply(other, (a, b) -> a - b);
    }

    private RLDimension apply(RLDimension other, BiFunction<Double, Double, Double> biFunction) {
        MeasureType otherType = other.getType();
        Double otherNumber = other.getNumber();

        if (!this.type.equals(otherType)) {
            throw new UnsupportedOperationException();
        }

        return new RLDimension(biFunction.apply(this.number, otherNumber), this.type);
    }

    @Override
    public String toString() {
        return String.format("%s%s", this.number, this.type.getExtension());
    }
}
