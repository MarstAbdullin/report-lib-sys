package ru.tkoinform.reportlib;

import com.sun.istack.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.JdbcUtils;
import ru.telecor.common.util.ExceptionUtils;
import ru.tkoinform.reportlib.builder.ReportBuilder;
import ru.tkoinform.reportlib.builder.ReportBuilderFactory;
import ru.tkoinform.reportlib.config.styling.StyleConfiguration;
import ru.tkoinform.reportlib.converter.ConverterFactory;
import ru.tkoinform.reportlib.db.DatabaseFacade;
import ru.tkoinform.reportlib.exception.ReportLibException;
import ru.tkoinform.reportlib.model.RLReportColumn;
import ru.tkoinform.reportlib.model.RLReportRequest;
import ru.tkoinform.reportlib.model.type.RLColumnType;
import ru.tkoinform.reportlib.parser.Any2Any;
import ru.tkoinform.reportlib.parser.ParserFactory;
import ru.tkoinform.reportlib.util.ParameterUtils;

import javax.sql.DataSource;
import java.io.File;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReportLib implements IReportLib {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private DatabaseFacade databaseFacade;
    private Path buildPath;

    /**
     * Создание экземпляра ReportLib
     *
     * @param dataSource БД
     * @param path папка, в которую будут сохраняться построенные отчеты
     */
    public ReportLib(DataSource dataSource, Path path) {
        this.databaseFacade = new DatabaseFacade(dataSource);
        this.buildPath = path;
    }

    @Override
    public File build(RLReportRequest request) throws ReportLibException {
        StyleConfiguration styleConfiguration = new StyleConfiguration();

        // Создание временного файла, в который будет писаться отчет
        Path absolute = buildPath.resolve(
                String.format(
                        "%s-%s.%s",
                        request.getName(),
                        request.getId(),
                        request.getFormat().getFileExtension()
                )
        );
        File tempFile = absolute.toFile();

        ReportBuilder builder = ReportBuilderFactory.createReportBuilder(request, tempFile, styleConfiguration);

        //Сохраняю запрос, чтоб вернуть его после построения
        String query = request.getQuery();


        if (request.getLimit() == null && ReportFormat.HTML.equals(request.getFormat())) {
            request.setLimit(RLConstants.Html.MAX_ROWS_COUNT);
        }

        // Перед шабкой вывод ошибки
        if (request.getLimit() != null) {
            try {
                Integer count = databaseFacade.getCountQuery(request);

                if (count > request.getLimit()) {
                    builder.writeError(String.format("Лимит количества строк = %s. Скачайте или распечатайте отчет, чтобы просмотреть его целиком.", request.getLimit()));

                    // Изменение запроса с добавлением ограничения
                    request.setQuery(request.getQuery() + "\nlimit " + request.getLimit());
                }
            } catch (Exception e) {
                throw new ReportLibException(String.format("Ошибка при подсчете количества строк: %s", ExceptionUtils.resolveException(e)), e);
            }
        }

        // Название
        builder.writeTitle();

        // Параметры отчета
        if (!Boolean.TRUE.equals(request.getHideParams()) && !ParameterUtils.allParamsEmpty(request)) {
            builder.writeParams();
        }

        // Номера колонок
        if (Boolean.TRUE.equals(request.getShowColumnNums())) {
            builder.writeColumnNums();
        }

        // Шапка таблицы
        builder.writeHeader();

        // Запрос строк из базы и заполнение строк таблицы
        databaseFacade.getQuery(request.getQuery(), request.getParameters(), (rs, i) -> {
            Runtime runtime = Runtime.getRuntime();
            logger.info("CURRENT MEMORY STOP: {}", runtime.freeMemory());

            rs.setFetchDirection(ResultSet.FETCH_FORWARD);

            // Инициализируем нужные парсеры
            // Внутри инициализации мы первый раз кликнули rs.next
            // Если rs.next пустй, то мы вернём Null

            //List<Any2Any<?, ?>> valueParsers = createParsers(request, rs);
            List<Any2Any<Object, Object>> valueParsers = createConverters(request, rs);

            // Начинаем большой цикл обхода ResultSet, если парсеры заполнились (и как следствие rs.next вернул
            // true), то есть результсет не пустой
            int rowIndex = 0;
            while (rs.next()) {
                if (Thread.currentThread().isInterrupted()) {
                    throw new RuntimeException("Current thread interrupted");
                }

                Object[] values = new Object[request.getColumns().size()];

                int columnIndex = 0;
                for (RLReportColumn column : request.getColumns()) {
                    try {
                        // TODO: косяк в типизации, подумать как исправить
                        Any2Any<Object, Object> formatter = (Any2Any<Object, Object>) valueParsers.get(columnIndex);

                        if (rs.getObject(column.getCode()) != null) {
                            values[columnIndex] = formatter.format(rs.getObject(column.getCode()));
                        } else {
                            values[columnIndex] = null;
                        }

                    } catch (Exception e) {
                        throw new ReportLibException(
                                String.format(
                                        "Ошибка при заполнении колонки '%s': %s",
                                        column.getName(),
                                        ExceptionUtils.resolveException(e)
                                ),
                                e
                        );
                    }
                    columnIndex++;
                }

                try {
                    builder.writeRow(values);

                } catch (Exception e) {
                    throw new ReportLibException(
                            String.format(
                                    "Ошибка при записи строки '%s': %s",
                                    rowIndex,
                                    ExceptionUtils.resolveException(e)
                            ),
                            e
                    );
                }

                rowIndex++;
            }

            JdbcUtils.closeResultSet(rs);
            return null;
        });

        builder.writeFooter();
        builder.complete();

        //Возвращаю запрос, если он был изменен
        request.setQuery(query);

        return tempFile;
    }

    /**
     * Формирование набора парсеров для набора колонок
     *
     * @param request запроос отчета
     * @param rs      результсет. Мы его откроем и считаем первую строку для того чтобы определить типы. Именно поэтому выше
     *                цикл будет do-while(rs.next) вместо классического while(rs.next)
     * @return набор готовых парсеров соответствующих по индексам колонкам
     */
    @Nullable
    private List<Any2Any<?, ?>> createParsers(RLReportRequest request, ResultSet rs) throws SQLException {
        List<Any2Any<?, ?>> valueFormatters = new ArrayList<>(request.getColumns().size());

        for (RLReportColumn column : request.getColumns()) {
            String className = rs.getMetaData().getColumnClassName(rs.findColumn(column.getCode()));

            logger.info("Column '{}' class is '{}'", column.getName(), className);

            Class<?> columnClass;
            try {
                columnClass = Class.forName(className);
            } catch (Exception e) {
                throw new ReportLibException(String.format("Не найден класс '%s' для колонки '%s'", className, column.getName()));
            }

            valueFormatters.add(
                    ParserFactory.createStringParser(
                            column,
                            request.getFormat(),
                            columnClass
                    )
            );
        }

        return valueFormatters;
    }

    @Nullable
    private <SRC, DST> List<Any2Any<SRC, DST>> createConverters(RLReportRequest request, ResultSet rs) throws SQLException {
        List<Any2Any<SRC, DST>> valueFormatters = new ArrayList<>(request.getColumns().size());

        for (RLReportColumn column : request.getColumns()) {
            String className = rs.getMetaData().getColumnClassName(rs.findColumn(column.getCode()));

            logger.info("Column '{}' class is '{}'", column.getName(), className);

            Class<SRC> columnClass;
            try {
                columnClass = (Class<SRC>) Class.forName(className);
            } catch (Exception e) {
                throw new ReportLibException(String.format("Не найден класс '%s' для колонки '%s'", className, column.getName()));
            }

            Class<DST> destinationClass = (Class<DST>) resolveDestinationClass(column.getType(), request.getFormat());

            valueFormatters.add(
                    ConverterFactory.createParser(
                            columnClass,
                            destinationClass,
                            request.getFormat(),
                            column
                    )
            );
        }

        return valueFormatters;
    }

    /**
     * Возвращает тип данных (класс) в который нужно преобразовать данные из БД
     * TODO: перенести в RLColumnType?
     *
     * @param columnType
     * @param reportFormat
     *
     * @return Class
     */
    private Class<?> resolveDestinationClass(RLColumnType columnType, ReportFormat reportFormat) {
        switch (columnType) {
            case IMAGE:
                if (ReportFormat.EXCEL_FORMATS.contains(reportFormat) || ReportFormat.HTML.equals(reportFormat)) {
                    return byte[].class;
                }

            case INTEGER:
                if (ReportFormat.EXCEL_FORMATS.contains(reportFormat) || ReportFormat.HTML.equals(reportFormat)) {
                    return Integer.class;
                }

            case FLOAT:
                if (ReportFormat.EXCEL_FORMATS.contains(reportFormat) || ReportFormat.HTML.equals(reportFormat)) {
                    return Double.class;
                }

            case BOOLEAN:
                if (ReportFormat.EXCEL_FORMATS.contains(reportFormat)) {
                    return Boolean.class;
                }

            case STRING:
            case DATE:
            case TIME:
            case DATETIME:
                return String.class;

            default:
                throw new ReportLibException(String.format("Не определен тип данных для типа колонки '%s' формата отчета '%s'", columnType, reportFormat));
        }
    }
}
