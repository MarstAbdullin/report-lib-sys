package ru.tkoinform.reportlib.builder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import ru.telecor.common.domain.DateHelper;
import ru.telecor.common.util.ExceptionUtils;
import ru.tkoinform.reportlib.RLConstants;
import ru.tkoinform.reportlib.config.styling.StyleConfiguration;
import ru.tkoinform.reportlib.exception.InternalReportException;
import ru.tkoinform.reportlib.model.RLReportColumn;
import ru.tkoinform.reportlib.model.RLReportParameter;
import ru.tkoinform.reportlib.model.RLReportRequest;
import ru.tkoinform.reportlib.model.type.RLColumnType;
import ru.tkoinform.reportlib.util.ImageUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportBuilderExcel extends AbstractReportBuilder {

    private Workbook workbook;
    private Sheet sheet;
    private int rowCounter = 0;
    private CellProperties[] columnStyles; // Кэш стилей колонок
    private Drawing drawing;


    public ReportBuilderExcel(RLReportRequest request, File tempFile, StyleConfiguration styleConfiguration, Workbook workbook) {
        super(request, tempFile, styleConfiguration);
        this.workbook = workbook;
        this.sheet = workbook.createSheet();
        // TODO: workbook list size
        this.columnStyles = new CellProperties[request.getColumns().size()];
        this.drawing = sheet.createDrawingPatriarch();
    }

    @Override
    public void writeTitle() {
        // Название отчета
        Row nameRow = sheet.createRow(rowCounter);
        Cell reportCell = nameRow.createCell(0);
        reportCell.setCellValue(request.getName());
        CellUtil.setCellStyleProperties(reportCell,
                CellProperties
                        .builder()
                        .borderStyle(BorderStyle.NONE)
                        .horizontalAlignment(HorizontalAlignment.CENTER)
                        .verticalAlignment(VerticalAlignment.CENTER)
                        .dataFormatCode((short) 0)
                        .color(IndexedColors.WHITE1.getIndex())
                        .build()
                        .mapProperties()
        );
        sheet.addMergedRegion(new CellRangeAddress(rowCounter, rowCounter, 0, request.getColumns().size() - 1));
        rowCounter++;

        // Дата
        Row dateRow = sheet.createRow(rowCounter);
        Cell dateCell = dateRow.createCell(0);
        dateCell.setCellValue("Дата формирования: " + DateHelper.fullDateFormat().format(new Date()));
        CellUtil.setCellStyleProperties(dateCell,
                CellProperties
                        .builder()
                        .borderStyle(BorderStyle.NONE)
                        .horizontalAlignment(HorizontalAlignment.RIGHT)
                        .verticalAlignment(VerticalAlignment.CENTER)
                        .dataFormatCode((short) 0)
                        .color(IndexedColors.WHITE1.getIndex())
                        .build()
                        .mapProperties()
        );
        sheet.addMergedRegion(new CellRangeAddress(rowCounter, rowCounter, 0, request.getColumns().size() - 1));
        rowCounter++;
    }

    @Override
    public void writeParams() {
        // Построение параметров отчета
        CellProperties parameterCellProperties = CellProperties
                .builder()
                .borderStyle(BorderStyle.NONE)
                .horizontalAlignment(HorizontalAlignment.LEFT)
                .verticalAlignment(VerticalAlignment.CENTER)
                .dataFormatCode((short) 0)
                .color(IndexedColors.WHITE1.getIndex())
                .build();

        for (RLReportParameter parameter : request.getParameters()) {
            Row parameterRow = sheet.createRow(rowCounter);
            Cell nameCell = parameterRow.createCell(0);
            nameCell.setCellValue(parameter.getName());
            CellUtil.setCellStyleProperties(nameCell,
                    parameterCellProperties.mapProperties()
            );

            // С какого номера ячейки начинается значение параметра
            final int valueStartsCellNumber = 1;
            //sheet.addMergedRegion(new CellRangeAddress(rowNumb, rowNumb, 0, valueStartsCellNumber - 1));

            Cell valueCell = parameterRow.createCell(valueStartsCellNumber);
            if (request.getColumns().size() - 1 > valueStartsCellNumber) {
                sheet.addMergedRegion(new CellRangeAddress(rowCounter, rowCounter, valueStartsCellNumber, request.getColumns().size() - 1));
            }
            CellUtil.setCellStyleProperties(valueCell,
                    parameterCellProperties.mapProperties()
            );
            valueCell.setCellValue(parameter.getDisplayableValue());

            rowCounter++;

            parameterRow.setHeight((short) -1); // Auto-size
        }
    }

    @Override
    public void writeColumnNums() {
        // Построение номеров колонок
        Row numbersRow = sheet.createRow(rowCounter);
        numbersRow.setHeight((short) (RLConstants.Excel.DEFAULT_ROW_HEIGHT * RLConstants.Excel.PIXEL_LENGTH));

        for (int i = 0; i < request.getColumns().size(); i++) {
            Cell cell = numbersRow.createCell(i);
            cell.setCellValue(i + 1);

            CellUtil.setCellStyleProperties(cell,
                    CellProperties
                            .builder()
                            .borderStyle(BorderStyle.THIN)
                            .horizontalAlignment(HorizontalAlignment.CENTER)
                            .verticalAlignment(VerticalAlignment.BOTTOM)
                            .dataFormatCode((short) 0)
                            .color(IndexedColors.GREY_25_PERCENT.getIndex())
                            .build()
                            .mapProperties()
            );
        }

        rowCounter++;
    }

    @Override
    public void writeHeader() {
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);

        List<RLReportColumn> columns = request.getColumns();

        // модификации для столбцов вне зависимости от наличия групп
        for (int currentColumnIndex = 0; currentColumnIndex < columns.size(); currentColumnIndex++) {
            RLReportColumn column = columns.get(currentColumnIndex);
            if (column.getWidth() != null) {
                double width = column.getWidth().getNumber() * column.getWidth().getType().getLengthInPixels() * RLConstants.Excel.PIXEL_LENGTH;
                sheet.setColumnWidth(currentColumnIndex, (int) width);
            }
        }

        Row row = sheet.createRow(rowCounter);
        row.setHeight((short) (RLConstants.Excel.DEFAULT_ROW_HEIGHT * RLConstants.Excel.PIXEL_LENGTH));

        boolean hasGroups = columns.stream().anyMatch(RLReportColumn::isGrouped);
        // модификации для столбцов в зависимости от наличия групп
        if (!hasGroups) {
            for (int currentColumnIndex = 0; currentColumnIndex < columns.size(); currentColumnIndex++) {
                RLReportColumn column = columns.get(currentColumnIndex);
                writeSingleHeader(currentColumnIndex, row, headerFont, column.getName());
            }
            rowCounter++;
        } else {
            Row subRow = sheet.createRow(rowCounter + 1);
            subRow.setHeight((short) (RLConstants.Excel.DEFAULT_ROW_HEIGHT * 2 * RLConstants.Excel.PIXEL_LENGTH));

            int groupingColumnsLeft = 0; // сколько колонок осталось добавить текущую группу
            for (int currentColumnIndex = 0; currentColumnIndex < columns.size(); currentColumnIndex++) {
                RLReportColumn column = columns.get(currentColumnIndex);

                if (column.isGrouped()) {
                    // пишем и название группы и названия заголовка
                    writeSingleHeader(currentColumnIndex, row, headerFont, column.getGroupName());
                    writeSingleHeader(currentColumnIndex, subRow, headerFont, column.getName());
                    sheet.addMergedRegion(new CellRangeAddress(rowCounter, rowCounter,
                            currentColumnIndex, currentColumnIndex + column.getGroupedCount() - 1));
                    groupingColumnsLeft = column.getGroupedCount();
                    groupingColumnsLeft--;
                } else {
                    if (groupingColumnsLeft > 0) { // входит ли в группу
                        writeSingleHeader(currentColumnIndex, subRow, headerFont, column.getName());
                        groupingColumnsLeft--;
                    } else {
                        writeSingleHeader(currentColumnIndex, row, headerFont, column.getName());
                        // увеличиваем столбик с заголовком
                        sheet.addMergedRegion(new CellRangeAddress(rowCounter, rowCounter + 1,
                                currentColumnIndex, currentColumnIndex));
                        // для корректного отображения в exel
                        writeSingleHeader(currentColumnIndex, subRow, headerFont, "");
                    }
                }
            }
            rowCounter += 2;
        }
    }

    private void writeSingleHeader(int currentColumnIndex, Row headerRow, Font headerFont, String name) {
        Cell cell = headerRow.createCell(currentColumnIndex);
        cell.setCellValue(name);
        CellUtil.setCellStyleProperties(cell,
                CellProperties
                        .builder()
                        .borderStyle(BorderStyle.THIN)
                        .horizontalAlignment(HorizontalAlignment.CENTER)
                        .verticalAlignment(VerticalAlignment.BOTTOM)
                        .dataFormatCode((short) 0)
                        .color(IndexedColors.GREY_25_PERCENT.getIndex())
                        .fontIndex(headerFont.getIndex())
                        .build()
                        .mapProperties()
        );
    }

    @Override
    public void writeRow(Object[] rowValues) {
        if (rowCounter > RLConstants.Excel.MAX_ROWS) {
            throw new IllegalArgumentException("Превышено максимальное количество строк для документа Excel!");
        }

        Row row = sheet.createRow(rowCounter);

        for (int i = 0; i < request.getColumns().size(); i++) {
            RLReportColumn column = request.getColumns().get(i);

            Cell cell = row.createCell(i, getCellType(column.getType()));
            setCellValue(rowValues[i], cell, column);

            if (columnStyles[i] == null) {
                columnStyles[i] = CellProperties
                        .builder()
                        .borderStyle(BorderStyle.THIN)
                        .horizontalAlignment(HorizontalAlignment.CENTER)
                        .dataFormatCode(getDataFormatCode(column))
                        .color(IndexedColors.WHITE1.getIndex())
                        .build();
            }

            CellUtil.setCellStyleProperties(cell,
                    columnStyles[i].mapProperties()
            );

            if (column.getType().equals(RLColumnType.IMAGE)) {
                row.setHeight(ImageUtils.getImageHeight(request));
                sheet.setColumnWidth(i, ImageUtils.getImageWidth(request));
            }
        }

        rowCounter++;
    }

    /**
     * Заполнение содержимого ячейки строкой
     *
     * @param objectValue
     * @param cell
     * @param column
     */
    private void setCellValue(Object objectValue, Cell cell, RLReportColumn column) {
        try {
            if (objectValue != null) {
                // Хак чтобы в ячейках все же были числа, булены и прочие типы а не строки
                if (objectValue.getClass() == Long.class) {
                    cell.setCellValue((Long) objectValue);

                } else if (objectValue.getClass() == Integer.class) {
                    cell.setCellValue((Integer) objectValue);

                } else if (objectValue.getClass() == Short.class) {
                    cell.setCellValue((Short) objectValue);

                } else if (objectValue.getClass() == Double.class) {
                    cell.setCellValue((Double) objectValue);

                } else if (objectValue.getClass() == Float.class) {
                    cell.setCellValue((Float) objectValue);

                } else if (objectValue.getClass() == Boolean.class) {
                    cell.setCellValue((Boolean) objectValue);

                } else if (objectValue.getClass() == Date.class) {
                    cell.setCellValue((Date) objectValue);

                } else if (objectValue.getClass() == String.class) {
                    cell.setCellValue((String) objectValue);

                } else if (objectValue.getClass() == byte[].class) {
                    insertImageToCell(rowCounter, cell.getColumnIndex(), (byte[]) objectValue);

                } else {
                    logger.error("Unsupported type: '{}'", objectValue.getClass().getSimpleName());
                    cell.setCellValue(objectValue.toString());
                }
            }
        } catch (Exception e) {
            throw new IllegalStateException(
                    String.format(
                            "Ошибка при заполнении колонки '%s': %s",
                            column.getName(),
                            ExceptionUtils.resolveException(e)
                    ),
                    e
            );
        }
    }

    private void insertImageToCell(int rowNumber, int colNumber, byte[] objectValue) {
        if (objectValue == null || objectValue.length == 0) {
            logger.warn("Empty image. Row number = {}, column number = {}", rowNumber, colNumber);
            return;
        }

        ClientAnchor clientAnchor = drawing.createAnchor(
                0,
                0,
                0,
                0,
                colNumber,
                rowNumber,
                colNumber + 1,
                rowNumber + 1
        );
        String contentType = ImageUtils.getContentType(objectValue);
        drawing.createPicture(
                clientAnchor,
                workbook.addPicture(objectValue, resolveImageType(contentType))
        );
    }

    @Override
    public void writeFooter() {

    }

    @Override
    public void complete() {
        try {
            if (sheet instanceof SXSSFSheet) {
                ((SXSSFSheet) sheet).flushRows();
            }

            FileOutputStream fileOutputStream = new FileOutputStream(tempFile);
            workbook.write(fileOutputStream);
            fileOutputStream.close();
            workbook.close();
        } catch (Exception e) {
            throw new InternalReportException("Error on writing workbook to file", e);
        }
    }

    @Override
    public void writeError(String errorMessage) {

    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    private static class CellProperties {
        private BorderStyle borderStyle;
        private HorizontalAlignment horizontalAlignment;
        private VerticalAlignment verticalAlignment;
        private Short dataFormatCode;
        private Short color;
        private Integer fontIndex;

        /**
         * Превратить в map, который можно использовать в POI
         *
         * @return properties map
         */
        private Map<String, Object> mapProperties() {
            Map<String, Object> properties = new HashMap<>();
            // Некоторые стили закомментированы - есть предположение что тормозит из-за них
            //properties.put(CellUtil.WRAP_TEXT, true);

            properties.put(CellUtil.BORDER_BOTTOM, this.borderStyle);
            properties.put(CellUtil.BORDER_LEFT, this.borderStyle);
            properties.put(CellUtil.BORDER_RIGHT, this.borderStyle);
            properties.put(CellUtil.BORDER_TOP, this.borderStyle);
            properties.put(CellUtil.BOTTOM_BORDER_COLOR, IndexedColors.BLACK);
            properties.put(CellUtil.LEFT_BORDER_COLOR, IndexedColors.BLACK);
            properties.put(CellUtil.RIGHT_BORDER_COLOR, IndexedColors.BLACK);
            properties.put(CellUtil.TOP_BORDER_COLOR, IndexedColors.BLACK);

            properties.put(CellUtil.DATA_FORMAT, this.dataFormatCode);

            if (this.verticalAlignment != null) {
                properties.put(CellUtil.VERTICAL_ALIGNMENT, this.verticalAlignment);
            }

            if (this.horizontalAlignment != null) {
                properties.put(CellUtil.ALIGNMENT, this.horizontalAlignment);
            }

            if (this.color != null) {
                properties.put(CellUtil.FILL_FOREGROUND_COLOR, this.color);
                properties.put(CellUtil.FILL_PATTERN, FillPatternType.SOLID_FOREGROUND);
            }

            if (this.fontIndex != null) {
                properties.put(CellUtil.FONT, this.fontIndex);
            }

            return properties;
        }
    }

    private CellType getCellType(RLColumnType type) {
        switch (type) {
            case INTEGER:
            case FLOAT:
                return CellType.NUMERIC;

            case BOOLEAN:
                return CellType.BOOLEAN;

            case STRING:
            case DATE:
            case TIME:
            case DATETIME:
            case IMAGE:
            default:
                return CellType.STRING;
        }
    }

    /**
     * Получить формат данных ячейки для указанной колонки отчета
     *
     * @param column
     * @return short format
     */
    private short getDataFormatCode(RLReportColumn column) {
        DataFormat dataFormat = workbook.createDataFormat();
        switch (column.getType()) {
            case STRING:
                return (short) 0;

            case INTEGER:
                //return (short) 1;
                return HSSFDataFormat.getBuiltinFormat("#,##0");

            case FLOAT:
                //return dataFormat.getFormat("#.##");
                //return (short) 4;
                return HSSFDataFormat.getBuiltinFormat("#,##0.00");

            case DATE:
                return dataFormat.getFormat("dd.MM.yyyy"); // 14

            case TIME:
                return dataFormat.getFormat("hh:mm:ss"); // 18, 20, 21

            case DATETIME:
                return dataFormat.getFormat("dd.MM.yyyy hh:mm:ss");

            case BOOLEAN:
                return (short) 0; // TODO: не могу подобрать формат чтобы тип был "логический"

            default:
                logger.info("Unknown column type: '{}'", column.getType());
                return (short) 0;
        }
    }

    private static int resolveImageType(String contentType) {
        switch (contentType) {
            case "image/png":
                return Workbook.PICTURE_TYPE_PNG;

            case "image/x-bitmap":
                return Workbook.PICTURE_TYPE_DIB;

            case "image/jpeg":
                return Workbook.PICTURE_TYPE_JPEG;

            default:
                throw new InternalReportException(String.format("Неизвестный тип формата изображения: %s", contentType));
        }
    }
}
