package ru.tkoinform.reportlib.test;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import ru.tkoinform.reportlib.ReportFormat;
import ru.tkoinform.reportlib.ReportLib;
import ru.tkoinform.reportlib.model.RLReportColumn;
import ru.tkoinform.reportlib.model.RLReportRequest;
import ru.tkoinform.reportlib.model.TestEntity;
import ru.tkoinform.reportlib.model.type.RLColumnType;
import ru.tkoinform.reportlib.repository.TestRepository;
import ru.tkoinform.reportlib.util.TestUtils;

import java.io.File;
import java.util.Arrays;
import java.util.List;

//@RunWith(SpringRunner.class)
@ExtendWith(SpringExtension.class)
@DataJpaTest
@EnableAutoConfiguration
@EnableTransactionManagement
@EntityScan(basePackages = {"ru.tkoinform.reportlib"})
@EnableJpaRepositories(basePackages = "ru.tkoinform.reportlib")
@ComponentScan(basePackages = "ru.tkoinform.reportlib")
@ContextConfiguration(classes = {TestRepository.class})
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2, replace = AutoConfigureTestDatabase.Replace.NONE)
public class ReportLibWithEmbeddedDbTests {

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setUp() {
        List<TestEntity> entities = TestUtils.mockEntities();
        testRepository.saveAll(entities);
        testRepository.flush();
    }

    @SneakyThrows
    public void testReport(ReportFormat reportFormat) {
        ReportLib reportLib = new ReportLib(jdbcTemplate.getDataSource(), new File(".").toPath());

        RLReportRequest request = TestUtils.mockRequest(reportFormat, TestUtils.mockParameters(), 2);
        reportLib.build(request);

        // Тест того же отчета, но с типами колонок STRING
        request.setName(String.format("%s_all_columns_string", request.getName()));
        for (RLReportColumn column : request.getColumns()) {
            column.setType(RLColumnType.STRING);
        }
        reportLib.build(request);
    }

    /**
     * Тест, запускающий все реализованные методы построения отчётов.
     */
    @Test
    public void testAllReports() {
        ReportFormat[] supportedValues = new ReportFormat[]{
                ReportFormat.XLS,
                ReportFormat.XLSX,
                ReportFormat.HTML,
                ReportFormat.CSV
        };

        Arrays.stream(supportedValues).forEach(this::testReport);
    }
}


