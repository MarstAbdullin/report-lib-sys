package ru.tkoinform.reportlib.builder;

import j2html.tags.ContainerTag;
import ru.tkoinform.reportlib.config.styling.StyleConfiguration;
import ru.tkoinform.reportlib.config.styling.elements.RLElementStyle;
import ru.tkoinform.reportlib.config.styling.elements.RLParameterDictStyle;
import ru.tkoinform.reportlib.config.styling.elements.RLTableStyle;
import ru.tkoinform.reportlib.exception.InternalReportException;
import ru.tkoinform.reportlib.model.RLReportColumn;
import ru.tkoinform.reportlib.model.RLReportParameter;
import ru.tkoinform.reportlib.model.RLReportRequest;
import ru.tkoinform.reportlib.util.FormatsHolder;
import ru.tkoinform.reportlib.util.ImageUtils;
import ru.tkoinform.reportlib.util.ObjectUtils;
import ru.tkoinform.reportlib.util.ParameterUtils;
import ru.tkoinform.reportlib.util.css.CssDeclaration;
import ru.tkoinform.reportlib.util.css.CssStyleSheet;

import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static j2html.TagCreator.*;

public class ReportBuilderHtml extends AbstractReportBuilder {

    private ContainerTag documentBody = div().withClass("rl-report-form");
    private ContainerTag table = table();

    public ReportBuilderHtml(RLReportRequest request, File tempFile, StyleConfiguration styleConfiguration) {
        super(request, tempFile, styleConfiguration);
    }

    @Override
    public void writeTitle() {
        FormatsHolder formatsHolder = new FormatsHolder();
        String creationTimeText = String.format("Дата формирования отчёта: %s", formatsHolder.getDateTimeFormat().format(new Date()));

        documentBody.with(
                div(request.getName())
                        .withClass("report-name full-width"),
                div(creationTimeText)
                        .withClass("report-date full-width")
        ).withStyle(
                String.format(
                        "width: %s; height: %s;",
                        this.request.getComputedWidth(),
                        this.request.getComputedHeight()
                )
        );
    }

    @Override
    public void writeParams() {
        // TODO: вынести этот фильтр в ReportLib?
        List<RLReportParameter> notEmptyParameters = request.getParameters()
                .stream()
                .filter((p) -> !ParameterUtils.isEmpty(p.getValue()))
                .collect(Collectors.toList());

        documentBody.with(
                div(each(notEmptyParameters, parameter -> div(
                        span(parameter.getName()).withClass("report-parameter-name"),
                        text(":"),
                        span(parameter.getDisplayableValue()).withClass("report-parameter-value")))
                ).withClass("report-parameter-dict")
        );
    }

    @Override
    public void writeColumnNums() {
        ContainerTag row = tr();
        for (int i = 0; i < request.getColumns().size(); i++) {
            row.with(
                    createTableHeaderCell(request.getColumns().get(i), String.valueOf(i))
            );
        }
        table.with(row);
    }

    @Override
    public void writeHeader() {
        // Шапка таблицы
        table.with(
                buildReportHeader(request.getColumns())
        );
    }

    private ContainerTag buildReportHeader(List<RLReportColumn> columns) {
        if (columns.stream().anyMatch(RLReportColumn::isGrouped)) {
            ContainerTag firstRow = tr();
            ContainerTag secondRow = tr();
            int countOfGroupedColumns = 0;
            for (RLReportColumn column : columns) {
                if (column.isGrouped()) {
                    countOfGroupedColumns = column.getGroupedCount();

                    firstRow.with(th(column.getGroupName()).attr("colspan", countOfGroupedColumns));
                    secondRow.with(createTableHeaderCell(column));

                    countOfGroupedColumns -= 1;
                } else if (countOfGroupedColumns != 0) {
                    countOfGroupedColumns -= 1;
                    secondRow.with(createTableHeaderCell(column));
                } else {
                    firstRow.with(createTableHeaderCell(column).attr("rowspan", 2));
                }
            }
            return div(firstRow, secondRow);

        } else {
            return tr(columns.stream()
                    .map(this::createTableHeaderCell)
                    .toArray(ContainerTag[]::new)
            ).withClass("rl-table-header");
        }
    }

    private ContainerTag createTableHeaderCell(RLReportColumn column) {
        return createTableHeaderCell(column, column.getName());
    }

    private ContainerTag createTableHeaderCell(RLReportColumn column, String content) {
        return th(content)
                .withStyle(Objects.nonNull(column.getWidth())
                        ? "width: " + column.getWidth()
                        : ""
                );
    }

    @Override
    public void writeRow(Object[] rowValues) {
        table.with(
                tr(Arrays.stream(rowValues)
                        .map(this::createCell)
                        .toArray(ContainerTag[]::new)
                )
        );
    }

    private ContainerTag createCell(Object objectValue) {
        if (objectValue != null) {
            if (ObjectUtils.isInteger(objectValue) || ObjectUtils.isFloat(objectValue)) {
                return td(objectValue.toString()).withClass("align-right");

            } else if (objectValue.getClass() == byte[].class) {
                return td(
                        img().withSrc(
                                String.format(
                                        "data:%s;base64,%s",
                                        ImageUtils.getContentType((byte[]) objectValue),
                                        Base64.getEncoder().encodeToString((byte[]) objectValue))
                        )
                        .attr("width", ImageUtils.getImageWidth(request))
                        .attr("height", ImageUtils.getImageHeight(request))
                );
            } else {

                return td(objectValue.toString()).withClass("");
            }
        } else {
            return td();
        }
    }

    @Override
    public void writeFooter() {

    }

    @Override
    public void complete() {
        String html = html(
                style(styles()),
                documentBody.with(
                        table.withClass("rl-table full-width pt-5")
                )
        ).render().replaceAll("&gt;", ">");

        try (FileWriter fileWriter = new FileWriter(tempFile)) {
            fileWriter.write(html);
            fileWriter.flush();

        } catch (Exception e) {
            throw new InternalReportException("Error on writing HTML to file", e);
        }
    }

    @Override
    public void writeError(String errorMessage) {
        documentBody.with(
                div(errorMessage).withClass("error-message")
        );
    }

    public String styles() {
        RLElementStyle header = styleConfiguration.getHeader();
        RLElementStyle creationDate = styleConfiguration.getCreationDate();
        RLParameterDictStyle parameterDict = styleConfiguration.getParameterDict();
        RLTableStyle rlTable = styleConfiguration.getTable();
        RLElementStyle head = rlTable.getHead();
        RLElementStyle cell = rlTable.getCell();
        RLElementStyle oddRow = rlTable.getOddRow();
        RLElementStyle error = styleConfiguration.getError();

        String additionalStyles = new CssStyleSheet()
                .addRule(".report-name", new CssDeclaration()
                        .color(header.getFontColor())
                        .fontFamily(header.getFontFamily())
                        .fontSize(header.getFontSize())
                        .minHeight(header.getMinHeight())
                        .marginLeft(header.getMarginLeft())
                        .marginBottom(header.getMarginBottom())
                        .onlyIf(header.isTextBold(), style ->
                                style.fontWeight("bold")))

                .addRule(".report-date", new CssDeclaration()
                        .height(creationDate.getHeight())
                        .fontSize(creationDate.getFontSize())
                        .fontFamily(creationDate.getFontFamily())
                        .color(creationDate.getFontColor())
                        .onlyIf(creationDate.isTextBold(), style ->
                                style.fontWeight("bold"))
                        .marginLeft(creationDate.getMarginLeft())
                        .marginBottom(header.getMarginBottom()))

                .addRule(".report-parameter-dict", new CssDeclaration()
                        .paddingTop(creationDate.getMarginBottom())
                        .minHeight(parameterDict.getMinHeight())
                        .color(parameterDict.getFontColor())
                        .fontFamily(parameterDict.getFontFamily())
                        .fontSize(parameterDict.getFontSize())
                        .marginLeft(parameterDict.getMarginLeft())
                        .paddingBottom(creationDate.getMarginBottom())
                        // для полосы слева от параметров
                        .marginBottom(parameterDict.getMarginBottom().minus(creationDate.getMarginBottom())))

                .addRule(".report-parameter-name", new CssDeclaration()
                        .onlyIf(parameterDict.getParameterName().isTextBold(), style ->
                                style.fontWeight("bold")))

                .addRule(".rl-table :is(th, td)", new CssDeclaration()
                        .borderWidth(rlTable.getBorderWidth())
                        .borderColor(rlTable.getBorderColor()))

                .addRule(".rl-table th", new CssDeclaration()
                        .backgroundColor(head.getBackgroundColor())
                        .color(head.getFontColor())
                        .fontFamily(head.getFontFamily())
                        .fontSize(head.getFontSize())
                        .onlyIf(head.isTextBold(), style ->
                                style.fontWeight("bold")))

                .addRule(".rl-table td", new CssDeclaration()
                        .minHeight(cell.getMinHeight())
                        .color(cell.getFontColor())
                        .fontFamily(cell.getFontFamily())
                        .fontSize(cell.getFontSize()))

                .addRule(".rl-table tr:nth-child(odd)", new CssDeclaration()
                        .backgroundColor(oddRow.getBackgroundColor()))

                .addRule(".error-message", new CssDeclaration()
                        .color(error.getFontColor())
                        .fontFamily(error.getFontFamily())
                        .fontSize(error.getFontSize())
                        .minHeight(error.getMinHeight())
                        .marginLeft(error.getMarginLeft())
                        .marginBottom(error.getMarginBottom())
                        .backgroundColor(error.getBackgroundColor())
                        .borderColor(error.getBorderColor())
                        .borderWidth(error.getBorderWidth())
                        .onlyIf(header.isTextBold(), style ->
                                style.fontWeight("bold")))

                .asCss();

        return this.styleConfiguration.styles() + "\n" + additionalStyles;
    }

}
