package ru.tkoinform.reportlib.util;

import org.apache.commons.lang3.RandomStringUtils;
import org.assertj.core.util.Lists;
import org.springframework.core.io.ClassPathResource;
import ru.tkoinform.reportlib.RLConstants;
import ru.tkoinform.reportlib.ReportFormat;
import ru.tkoinform.reportlib.model.RLDimension;
import ru.tkoinform.reportlib.model.RLReportColumn;
import ru.tkoinform.reportlib.model.RLReportParameter;
import ru.tkoinform.reportlib.model.RLReportRequest;
import ru.tkoinform.reportlib.model.TestEntity;
import ru.tkoinform.reportlib.model.type.ListType;
import ru.tkoinform.reportlib.model.type.MeasureType;
import ru.tkoinform.reportlib.model.type.RLColumnType;
import ru.tkoinform.reportlib.model.type.RLParameterType;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class TestUtils {

    /**
     * Эмуляция запроса отчета
     *
     * @param reportFormat
     * @param parameters
     * @return
     */
    public static RLReportRequest mockRequest(ReportFormat reportFormat, List<RLReportParameter> parameters, Integer limit) {
        RLReportRequest request = new RLReportRequest();
        request.setId(UUID.fromString("00000000-0000-0000-0000-000000000000"));

        // Колонки
        RLReportColumn longColumn = new RLReportColumn();
        longColumn.setName("long");
        longColumn.setCode("id");
        longColumn.setType(RLColumnType.INTEGER);
        longColumn.setGroupedCount(5);
        longColumn.setGroupName("numbers");
        longColumn.setWidth(new RLDimension(5D, MeasureType.CM));

        RLReportColumn integerColumn = new RLReportColumn();
        integerColumn.setName("integer");
        integerColumn.setCode("test_integer");
        integerColumn.setType(RLColumnType.INTEGER);
        integerColumn.setWidth(new RLDimension(5D, MeasureType.CM));

        RLReportColumn floatColumn = new RLReportColumn();
        floatColumn.setName("float");
        floatColumn.setCode("test_float");
        floatColumn.setType(RLColumnType.FLOAT);
        floatColumn.setWidth(new RLDimension(5D, MeasureType.CM));

        RLReportColumn doubleColumn = new RLReportColumn();
        doubleColumn.setName("double");
        doubleColumn.setCode("test_double");
        doubleColumn.setType(RLColumnType.FLOAT);
        doubleColumn.setWidth(new RLDimension(5D, MeasureType.CM));

        RLReportColumn bigdecimalColumn = new RLReportColumn();
        bigdecimalColumn.setName("bigdecimal");
        bigdecimalColumn.setCode("test_bigdecimal");
        bigdecimalColumn.setType(RLColumnType.FLOAT);
        bigdecimalColumn.setWidth(new RLDimension(5D, MeasureType.CM));

        RLReportColumn timeColumn = new RLReportColumn();
        timeColumn.setName("time");
        timeColumn.setCode("test_time");
        timeColumn.setType(RLColumnType.TIME);
        timeColumn.setGroupedCount(5);
        timeColumn.setGroupName("dates");
        timeColumn.setWidth(new RLDimension(150D, MeasureType.PX));

        RLReportColumn dateColumn = new RLReportColumn();
        dateColumn.setName("date");
        dateColumn.setCode("test_date");
        dateColumn.setType(RLColumnType.DATE);
        dateColumn.setWidth(new RLDimension(150D, MeasureType.PX));

        RLReportColumn dateTimeColumn = new RLReportColumn();
        dateTimeColumn.setName("datetime");
        dateTimeColumn.setCode("test_datetime");
        dateTimeColumn.setType(RLColumnType.DATETIME);
        dateTimeColumn.setWidth(new RLDimension(300D, MeasureType.PX));

        RLReportColumn datesqlColumn = new RLReportColumn();
        datesqlColumn.setName("datesql");
        datesqlColumn.setCode("test_datesql");
        datesqlColumn.setType(RLColumnType.DATE);
        datesqlColumn.setWidth(new RLDimension(150D, MeasureType.PX));

        RLReportColumn intervalColumn = new RLReportColumn();
        intervalColumn.setName("interval");
        intervalColumn.setCode("test_interval");
        intervalColumn.setType(RLColumnType.TIME);
        intervalColumn.setWidth(new RLDimension(150D, MeasureType.PX));

        RLReportColumn stringColumn = new RLReportColumn();
        stringColumn.setName("string");
        stringColumn.setCode("test_string");
        stringColumn.setType(RLColumnType.STRING);

        RLReportColumn booleanColumn = new RLReportColumn();
        booleanColumn.setName("boolean");
        booleanColumn.setCode("test_boolean");
        booleanColumn.setType(RLColumnType.BOOLEAN);
        booleanColumn.setWidth(new RLDimension(75D, MeasureType.PX));

        RLReportColumn imageColumn = new RLReportColumn();
        imageColumn.setName("image");
        imageColumn.setCode("test_image");
        imageColumn.setType(RLColumnType.IMAGE);

        RLReportColumn blobColumn = new RLReportColumn();
        blobColumn.setName("blob");
        blobColumn.setCode("test_blob");
        blobColumn.setType(RLColumnType.IMAGE);

        request.setColumns(
                Lists.newArrayList(
                        longColumn,
                        integerColumn,
                        floatColumn,
                        doubleColumn,
                        bigdecimalColumn,
                        timeColumn,
                        dateColumn,
                        dateTimeColumn,
                        datesqlColumn,
                        intervalColumn,
                        stringColumn,
                        booleanColumn,
                        imageColumn,
                        blobColumn
                )
        );

        // Параметры
        request.setParameters(parameters);

        request.setQuery(
                "-- comment \nSELECT " +
                        "id, " +
                        "test_integer, " +
                        "test_float, " +
                        "test_double, " +
                        "test_bigdecimal, " +
                        "test_time, " +
                        "test_date, " +
                        "test_datetime, " +
                        "test_datesql, " +
                        "test_date - test_time as test_interval, " +
                        "\n--test_date - test_time as test_interval, \n" +
                        "/*test_string, " +
                        "test_string\n, " +
                        "-- comment" +
                        "test_string\n, " +
                        "test_string,*/ " +
                        "test_string, " +
                        "test_boolean, " +
                        "test_image, " +
                        "test_blob " +
                        "FROM TEST_ENTITY \n"
        );
        if (parameters != null && parameters.size() > 0) {
            request.setQuery(request.getQuery()  +
                    "WHERE test_string in (:test_strings) " +
                    "and test_integer in (:test_integers) -- comment"
            );
        }
        // TODO: тест с незаполненными параметрами (автозамена на true)

        request.setFormat(reportFormat);
        request.setName("test");
        request.setHideParams(false);
        request.setShowColumnNums(true);
        request.setSize(ListType.A3);
        request.setLandscape(true);

        request.setSettings(new HashMap<>());
        request.getSettings().put(RLConstants.RLReportRequestSettingsKeys.CSV_DELIMITER, RLConstants.Csv.DELIMITER.toString());
        request.getSettings().put(RLConstants.RLReportRequestSettingsKeys.CSV_ENCODING, RLConstants.Csv.ENCODING);
        request.getSettings().put(RLConstants.RLReportRequestSettingsKeys.IMAGE_HEIGHT, "125");

        request.setLimit(limit);

        return request;
    }

    /**
     * Эмулировать запрос отчета без параметров
     *
     * @param reportFormat
     * @return
     */
    public static RLReportRequest mockRequest(ReportFormat reportFormat) {
        return mockRequest(reportFormat, Collections.emptyList(), null);
    }

    public static byte[] loadImage(String imageName) {
        try {
            return Files.readAllBytes(new ClassPathResource(imageName).getFile().toPath());
        } catch (IOException e) {
            throw new IllegalStateException("Error on loading image " + imageName, e);
        }
    }

    /**
     * Эмулировать параметры отчета
     *
     * @return
     */
    public static List<RLReportParameter> mockParameters() {
        RLReportParameter stringParameter = new RLReportParameter();
        stringParameter.setArray(true);
        stringParameter.setName("Строки");
        stringParameter.setCode("test_strings");
        stringParameter.setValue(new String[]{"string2", "string4", "string3", "test\"test", "test|test"});
        stringParameter.setDisplayableValue("string2, string4, string3, test\"test, test|test");
        stringParameter.setType(RLParameterType.STRING);

        RLReportParameter integerParameter = new RLReportParameter();
        integerParameter.setName("Целочисленные значения");
        integerParameter.setArray(true);
        integerParameter.setCode("test_integers");
        integerParameter.setValue(new Integer[]{2000, 30000, 3000000});
        integerParameter.setDisplayableValue("2000, 300000, 3000000");
        integerParameter.setType(RLParameterType.INTEGER);

        return Lists.newArrayList(stringParameter, integerParameter);
    }

    /**
     * Эмулировать набор тестовых записей для таблицы
     *
     * @return
     */
    public static List<TestEntity> mockEntities() {
        return Arrays.asList(
                TestEntity
                        .builder()
                        .integer(1)
                        .afloat(1.1F)
                        .adouble(1.1D)
                        .bigdecimal(new BigDecimal(1.1))
                        .date(new Date())
                        .time(new Date())
                        .datetime(new Date())
                        //TODO: В дебаге возвращает null
                        .datesql(new java.sql.Date(new Date().getTime()))
                        .string("string1")
                        .bool(true)
                        .image(loadImage("RRS23.png"))
                        .blob(loadImage("RRS23.png"))
                        .build(),
                TestEntity
                        .builder()
                        .integer(2000)
                        .afloat(11212.2299999999F)
                        .adouble(11212.2299999999D)
                        .bigdecimal(new BigDecimal(11212.2299999999))
                        .date(new Date())
                        .time(new Date())
                        .datetime(new Date())
                        .string("string2")
                        .bool(false)
                        .image(loadImage("RRS23.png"))
                        .blob(loadImage("RRS23.png"))
                        .build(),
                TestEntity
                        .builder()
                        .integer(3000000)
                        .afloat(0F)
                        .adouble(1.3D)
                        .bigdecimal(new BigDecimal(1.3F))
                        .date(new Date())
                        .time(new Date())
                        .datetime(new Date())
                        .datesql(new java.sql.Date(new Date().getTime()))
                        .string("string3")
                        .bool(true)
                        .image(loadImage("RR22.png"))
                        .blob(loadImage("RR22.png"))
                        .build(),
                TestEntity
                        .builder()
                        .integer(3000000)
                        .afloat(0F)
                        .adouble(1.3D)
                        .bigdecimal(new BigDecimal(1.3F))
                        .date(new Date())
                        .time(new Date())
                        .datetime(new Date())
                        .datesql(new java.sql.Date(new Date().getTime()))
                        .string("string3")
                        .bool(true)
                        .image(loadImage("RRS23.png"))
                        .blob(loadImage("RRS23.png"))
                        .build(),
                TestEntity
                        .builder()
                        .integer(3000000)
                        .afloat(0F)
                        .adouble(1.3D)
                        .bigdecimal(new BigDecimal(1.3F))
                        .date(new Date())
                        .time(new Date())
                        .datetime(new Date())
                        .datesql(new java.sql.Date(new Date().getTime()))
                        .string("test|test")
                        .bool(true)
                        .image(loadImage("RRS23.png"))
                        .blob(loadImage("RRS23.png"))
                        .build(),
                TestEntity
                        .builder()
                        .integer(3000000)
                        .afloat(0F)
                        .adouble(1.3D)
                        .bigdecimal(new BigDecimal(1.3F))
                        .date(new Date())
                        .time(new Date())
                        .datetime(new Date())
                        .datesql(new java.sql.Date(new Date().getTime()))
                        .string("test\"test")
                        .bool(true)
                        .image(loadImage("RRS23.png"))
                        .blob(loadImage("RRS23.png"))
                        .build()
        );
    }

    /**
     * Эмулировать набор случайных тестовых записей для таблицы
     *
     * @return
     */
    public static List<TestEntity> mockEntities(int count) {
        List<TestEntity> testEntities = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            testEntities.add(
                    TestEntity
                            .builder()
                            .integer((int) (Math.random() * 100))
                            .afloat((float) (Math.random() * 100))
                            .adouble(Math.random() * 100)
                            .bigdecimal(new BigDecimal(Math.random() * 100))
                            .date(new Date())
                            .time(new Date())
                            .datetime(new Date())
                            .datesql(new java.sql.Date(new Date().getTime()))
                            .string(RandomStringUtils.random(32))
                            .bool(Math.random() > 0.5)
                            //.image(loadImage("RRS23.png"))
                            //.blob(loadImage("RRS23.png"))
                            .build()
            );
        }

        return testEntities;
    }
}
