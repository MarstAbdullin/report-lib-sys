package ru.tkoinform.reportlib.db;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import ru.telecor.common.domain.DateInterval;
import ru.telecor.common.domain.TimeInterval;
import ru.tkoinform.reportlib.RLConstants;
import ru.tkoinform.reportlib.model.RLReportParameter;
import ru.tkoinform.reportlib.model.RLReportRequest;
import ru.tkoinform.reportlib.model.type.RLParameterType;
import ru.tkoinform.reportlib.util.ParameterUtils;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DatabaseFacade {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private ObjectMapper objectMapper = new ObjectMapper();

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public DatabaseFacade(DataSource dataSource) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.setFetchSize(100000);
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    /**
     * Обращение к базе с запросом.
     *
     * @param query
     * @param parameters
     * @param rm
     * @param <T>
     * @return результат выполнения запроса
     */
    @Transactional(readOnly = true)
    public <T> Stream<T> getQuery(String query, List<RLReportParameter> parameters, RowMapper<T> rm) {
        MapSqlParameterSource sqlParameterSource = fillSqlParameters(parameters);
        query = replaceEmptyParameters(query, parameters);

        // TODO: как работает MapSqlParameterSource? Заменяет :CODE на значения Map?
        return namedParameterJdbcTemplate.queryForStream(query, sqlParameterSource, rm);
    }

    /**
     * Обращение к базе с целью выяснения количества записей в отчёте.
     *
     * @param request
     * @return количество строк
     */
    @Transactional(readOnly = true)
    public Integer getCountQuery(RLReportRequest request) {
        // TODO: ускорить запрос убрав вложенный селект и заменив алиасы на count(*)
        String countQuery = "SELECT count(*) FROM (\n" + request.getQuery() + "\n) as count_query_alias";

        return getQuery(countQuery, request.getParameters(), (resultSet, i) ->
                resultSet.getInt(1)
        ).collect(Collectors.toList()).get(0);
    }

    /**
     * Замена всех пустых обязательных параметров на true
     * <p>
     * Например: WHERE customer_id IN (:IDS)
     * Код параметра: IDS
     * sql=customer_id IN (:IDS)
     * <p>
     * Если параметр не обязательный, то в запросе вместо "customer_id IN (:IDS)" будет true
     *
     * @param query
     * @param parameters
     * @return отредактированный запрос
     */
    private String replaceEmptyParameters(String query, List<RLReportParameter> parameters) {
        for (RLReportParameter parameter : parameters) {
            logger.info(
                    "Parameter: " + parameter.getCode()
                            + ", SQL: " + parameter.getSql()
                            + ", Required: " + parameter.getRequired()
                            + ", Value: " + parameter.getValue()
            );

            if (ParameterUtils.isEmpty(parameter.getValue())) {
                if (Boolean.TRUE.equals(parameter.getRequired())) {
                    throw new IllegalStateException(String.format("Не заполнен обязательный параметр: \"%s\"", parameter.getName()));
                }

                // Говорят что здесь нужно проверять обязательность - Boolean.TRUE.equals(parameter.getRequired())
                // но по факту заменять нужно всегда, т.к. обязательность часто не указывают
                if (parameter.getSql() != null) {
                    // Проверяем правильность теста автозамены параметра
                    if (!parameter.getSql().contains(String.format(":%s", parameter.getCode()))) {
                        throw new IllegalStateException(String.format("Ошибка в SQL-коде параметра \"%s\": не содержит кода параметра", parameter.getName()));
                    }

                    // Проверяем что в запросе отчета есть необходимая часть которую можно заменить
                    int foundAt = query.indexOf(parameter.getSql());
                    if (foundAt == -1) {
                        throw new IllegalStateException(String.format("Ошибка в SQL-коде параметра \"%s\": в отчете не найдено место для автозамены", parameter.getName()));
                    }

                    query = query.replace(parameter.getSql(), "true");
                }
            }
        }

        return query;
    }


    /**
     * Конвертация параметров из массива java-обьектов в MapSqlParameterSource который пойдет в запрос
     *
     * @param parameters
     * @param <D>
     * @return MapSqlParameterSource
     */
    private <D> MapSqlParameterSource fillSqlParameters(List<RLReportParameter> parameters) {
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();

        // TODO: это должно выполняться при старте приложения (нужен бин objectMapper)
        objectMapper.registerModule(new JodaModule());

        for (RLReportParameter parameter : parameters) {
            if (ParameterUtils.isEmpty(parameter.getValue())) {
                // Пропускаем пустые параметры
                continue;
            }

            Class<?> dataTypeClass = ParameterUtils.resolveClass(parameter.getType());

            if (parameter.isArray()) {
                List<D> values = objectMapper.convertValue(
                        parameter.getValue(),
                        objectMapper.getTypeFactory().constructCollectionType(List.class, dataTypeClass)
                );
                sqlParameterSource.addValue(parameter.getCode(), values);

            } else {
                // TODO: косяк типизации, подумать как исправить
                D value = objectMapper.convertValue(
                        parameter.getValue(),
                        (Class<D>) dataTypeClass
                );

                if (RLParameterType.DATE_INTERVAL.equals(parameter.getType())) {
                    // TODO: возможно это уже делается в GM, проверить
                    DateInterval dateInterval = (DateInterval) value;
                    sqlParameterSource.addValue(parameter.getCode() + RLConstants.ReportParameter.INTERVAL_BEGIN, dateInterval.getBegin());
                    sqlParameterSource.addValue(parameter.getCode() + RLConstants.ReportParameter.INTERVAL_END, dateInterval.getEnd());

                } else if (RLParameterType.TIME_INTERVAL.equals(parameter.getType())) {
                    // TODO: возможно это уже делается в GM, проверить
                    TimeInterval timeInterval = (TimeInterval) value;
                    sqlParameterSource.addValue(parameter.getCode() + RLConstants.ReportParameter.INTERVAL_BEGIN, timeInterval.getBegin());
                    sqlParameterSource.addValue(parameter.getCode() + RLConstants.ReportParameter.INTERVAL_END, timeInterval.getEnd());

                } else if (RLParameterType.DATE.equals(parameter.getType())) {
                    LocalDate date = (LocalDate) value;
                    sqlParameterSource.addValue(parameter.getCode(), date.toDate());

                } else {
                    sqlParameterSource.addValue(parameter.getCode(), value);
                }
            }
        }

        return sqlParameterSource;
    }
}
