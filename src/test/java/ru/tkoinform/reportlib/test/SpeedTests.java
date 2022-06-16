package ru.tkoinform.reportlib.test;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.util.StopWatch;
import ru.tkoinform.reportlib.ReportFormat;
import ru.tkoinform.reportlib.ReportLib;
import ru.tkoinform.reportlib.db.DatabaseFacade;
import ru.tkoinform.reportlib.model.RLReportRequest;
import ru.tkoinform.reportlib.model.TestEntity;
import ru.tkoinform.reportlib.repository.TestRepository;
import ru.tkoinform.reportlib.util.TestUtils;

import java.io.File;
import java.util.List;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@EnableAutoConfiguration
@EnableTransactionManagement
@EntityScan(basePackages = {"ru.tkoinform.reportlib"})
@EnableJpaRepositories(basePackages = "ru.tkoinform.reportlib")
@ComponentScan(basePackages = "ru.tkoinform.reportlib")
@ContextConfiguration(classes = {DatabaseFacade.class, TestRepository.class})
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2, replace = AutoConfigureTestDatabase.Replace.NONE)
public class SpeedTests {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private DatabaseFacade databaseFacade;

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setUp() {
        testRepository.deleteAll();
        testRepository.flush();
        List<TestEntity> entities = TestUtils.mockEntities(1000000);
        testRepository.saveAll(entities);
        testRepository.flush();
    }

    @Test
    @SneakyThrows
    public void stressTest() {
        Runtime runtime = Runtime.getRuntime();
        logger.info("CURRENT MEMORY START: {}", runtime.freeMemory());
        ReportLib reportLib = new ReportLib(jdbcTemplate.getDataSource(), new File(".").toPath());

        final int cycles = 1;
        StopWatch stopWatch = new StopWatch();

        for (int i = 0; i < cycles; i++) {
            this.setUp();
            stopWatch.start("CYCLE " + (i + 1));

            RLReportRequest request = TestUtils.mockRequest(ReportFormat.CSV);
            reportLib.build(request);

            stopWatch.stop();
        }

        logger.info("TOTAL TIME: {}", stopWatch.prettyPrint());
    }
}
