package ru.tkoinform.reportlib.util;

import ru.tkoinform.reportlib.RLConstants;
import ru.tkoinform.reportlib.ReportFormat;
import ru.tkoinform.reportlib.exception.ReportLibException;
import ru.tkoinform.reportlib.model.RLReportRequest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLConnection;

public final class ImageUtils {

    private ImageUtils() {
        throw new UnsupportedOperationException();
    }

    public static String getContentType(byte[] objectValue) {
        try {
            return URLConnection.guessContentTypeFromStream(
                    new ByteArrayInputStream(objectValue)
            );
        } catch (IOException e) {
            throw new ReportLibException("Ошибка при получении типа изображения", e);
        }
    }

    public static short getImageHeight(RLReportRequest request) {
        String imageHeightSetting = request.getSettings().get(RLConstants.RLReportRequestSettingsKeys.IMAGE_HEIGHT);

        if (request.getFormat().equals(ReportFormat.HTML)) {
            return imageHeightSetting != null ? Short.parseShort(imageHeightSetting) : RLConstants.Html.IMAGE_HEIGHT_HTML;
        } else {
            return imageHeightSetting != null ? (short) (Short.parseShort(imageHeightSetting) * RLConstants.Excel.COEFFICIENT_EXCEL) : RLConstants.Excel.IMAGE_HEIGHT_EXCEL;
        }
    }

    public static short getImageWidth(RLReportRequest request) {
        return (short) (getImageHeight(request) * (request.getFormat().equals(ReportFormat.HTML)
                ? RLConstants.Html.HEIGHT_TO_WIDTH_COEFFICIENT_HTML
                : RLConstants.Excel.HEIGHT_TO_WIDTH_COEFFICIENT_EXCEL));
    }
}
