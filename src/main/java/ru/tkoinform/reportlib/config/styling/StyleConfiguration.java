package ru.tkoinform.reportlib.config.styling;

import lombok.Getter;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.FileCopyUtils;
import ru.tkoinform.reportlib.RLConstants;
import ru.tkoinform.reportlib.config.styling.elements.RLElementStyle;
import ru.tkoinform.reportlib.config.styling.elements.RLParameterDictStyle;
import ru.tkoinform.reportlib.config.styling.elements.RLTableStyle;
import ru.tkoinform.reportlib.model.RLDimension;
import ru.tkoinform.reportlib.model.type.MeasureType;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;

import static ru.tkoinform.reportlib.model.RLDimension.valued;

/**
 * Макет отчета
 *
 * Билдеры отчетов должны опираться на этот макет для формирования отчетов
 */
@Getter
public class StyleConfiguration {

    private final RLElementStyle header;

    private final RLElementStyle creationDate;

    private final RLParameterDictStyle parameterDict;

    private final RLTableStyle table;

    private final RLElementStyle error;

    private final RLDimension size1em = valued(1, MeasureType.SYMBOLS);
    private final RLDimension size2pt = valued(2, MeasureType.PT);
    private final RLDimension size6pt = valued(6, MeasureType.PT);
    private final RLDimension size9pt = valued(9, MeasureType.PT);
    private final RLDimension size12pt = valued(12, MeasureType.PT);
    private final RLDimension size14pt = valued(14, MeasureType.PT);
    private final RLDimension size16pt = valued(16, MeasureType.PT);
    private final RLDimension size24pt = valued(24, MeasureType.PT);
    private final RLDimension size26pt = valued(26, MeasureType.PT);
    private final RLDimension size18pt = valued(18, MeasureType.PT);

    public StyleConfiguration() {

        header = RLElementStyle
                .builder()
                .fontSize(size12pt)
                .fontFamily(RLConstants.FontFamily.CALIBRI)
                .fontColor("#203764")
                .isTextBold(true)
                .minHeight(size24pt)
                .marginLeft(size1em)
                .marginBottom(size6pt)
                .build();


        creationDate = RLElementStyle
                .builder()
                .height(size18pt)
                .fontSize(size12pt)
                .fontFamily(RLConstants.FontFamily.CALIBRI)
                .fontColor("#203764")
                .isTextBold(true)
                .marginLeft(size1em)
                .marginBottom(size6pt)
                .build();


        parameterDict = RLParameterDictStyle
                .builder()
                .minHeight(size26pt)
                .marginBottom(size16pt)
                .marginLeft(size1em)
                .fontColor(RLConstants.Color.BLACK)
                .fontFamily(RLConstants.FontFamily.CALIBRI)
                .fontSize(size9pt)
                .parameterName(RLElementStyle
                        .builder()
                        .isTextBold(true)
                        .build())
                .build();

        table = RLTableStyle
                .builder()
                .borderColor(RLConstants.Color.WHITE)
                .borderWidth(size2pt)
                .oddRow(RLElementStyle
                        .builder()
                        .backgroundColor("#ECF2FE")
                        .build())
                .head(RLElementStyle
                        .builder()
                        .backgroundColor("#D2E0FA")
                        .fontSize(size9pt)
                        .fontFamily(RLConstants.FontFamily.CALIBRI)
                        .fontColor(RLConstants.Color.BLACK)
                        .isTextBold(true)
                        .build())
                .cell(RLElementStyle
                        .builder()
                        .fontSize(size9pt)
                        .fontFamily(RLConstants.FontFamily.CALIBRI)
                        .fontColor(RLConstants.Color.BLACK)
                        .minHeight(size14pt)
                        .build())
                .build();

        error = RLElementStyle.builder()
                .fontSize(size12pt)
                .fontFamily(RLConstants.FontFamily.CALIBRI)
                .fontColor("darkred")
                .isTextBold(true)
                .marginBottom(size6pt)
                .borderWidth(RLDimension.valued(2, MeasureType.PX))
                .borderColor("darkred")
                .backgroundColor("mistyrose")
                .build();
    }

    public String styles() {
        ResourceLoader resourceLoader = new DefaultResourceLoader();

        Resource resource = resourceLoader.getResource("classpath:css/main.css");

        try (Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.US_ASCII)) {
            return FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
