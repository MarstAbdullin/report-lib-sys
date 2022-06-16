package ru.tkoinform.reportlib.test;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import ru.tkoinform.reportlib.ReportFormat;
import ru.tkoinform.reportlib.db.DatabaseFacade;
import ru.tkoinform.reportlib.model.TestEntity;
import ru.tkoinform.reportlib.repository.TestRepository;
import ru.tkoinform.reportlib.util.TestUtils;

import java.util.Collections;
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
public class DatabaseTests {

    @Autowired
    private DatabaseFacade databaseFacade;

    @Autowired
    private TestRepository testRepository;

    @BeforeEach
    public void setUp() {
        List<TestEntity> entities = TestUtils.mockEntities();
        testRepository.saveAll(entities);
        testRepository.flush();
    }

    /**
     * Тест на проверку количества записей в отчёте.
     * Работает только с тестовыми данными, введенными в методе
     *
     * @see ReportLibWithEmbeddedDbTests#setUp()
     */
    @Test
    @SneakyThrows
    public void testCountQuery() {
        // TODO: сравнивать с числом, рассчитанным по entities.size() и request.getParameters() а не с константой
        Assertions.assertEquals(databaseFacade.getCountQuery(TestUtils.mockRequest(ReportFormat.XLS, TestUtils.mockParameters(), null)), (Integer) 5);
    }

    /**
     * Тест попытки вставки данных в базе через запрос
     * Транзакция должна быть readonly, отчетная система не должна иметь возможность
     * записи в базу.
     */
    @Test
    @SneakyThrows
    public void testWriteDatabase() {
        Assertions.assertThrows(Exception.class, () -> {
            String insertQuery = "INSERT INTO TEST_ENTITY (test_string, test_integer, test_float, test_date) VALUES ('test', 0, 0, '2021-11-18 00:00:00')";
            databaseFacade.getQuery(insertQuery, Collections.emptyList(), (rs, i) ->
                    rs.next() ? rs.getInt(1) : -1
            );
        });
    }

    /**
     * Тест попытки изменения данных в базе через запрос
     * Транзакция должна быть readonly, отчетная система не должна иметь доступа
     * к изменению данных в базе.
     */
    @Test
    @SneakyThrows
    public void testUpdateDatabase() {
        Assertions.assertThrows(Exception.class, () -> {
            String updateQuery = "UPDATE TEST_ENTITY SET test_string = 'test'";
            databaseFacade.getQuery(updateQuery, Collections.emptyList(), (rs, i) ->
                    rs.next() ? rs.getInt(1) : -1
            );
        });
    }
}
