package ru.otus;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.otus.cachehw.DbServiceClientWithCacheImpl;
import ru.otus.core.repository.executor.DbExecutorImpl;
import ru.otus.core.sessionmanager.TransactionRunnerJdbc;
import ru.otus.crm.datasource.DriverManagerDataSource;
import ru.otus.crm.model.Client;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.crm.service.DbServiceClientImpl;
import ru.otus.jdbc.mapper.*;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.stream.LongStream;

@DisplayName("Сравниваем скорость работы с cache и без ")
@Testcontainers
class DBServiceWithOrWithoutCacheTest {
    private static final Logger logger = LoggerFactory.getLogger(DBServiceWithOrWithoutCacheTest.class);

    private DBServiceClient serviceWithCache;
    private DBServiceClient serviceWithoutCache;

    @Container
    private final PostgreSQLContainer<?> postgresqlContainer = new PostgreSQLContainer<>("postgres:12-alpine")
            .withDatabaseName("testDataBase")
            .withUsername("owner")
            .withPassword("secret")
            .withClasspathResourceMapping("00_createTables.sql", "/docker-entrypoint-initdb.d/00_createTables.sql", BindMode.READ_ONLY);

    @BeforeEach
    void setUp() {
        var dataSource = new DriverManagerDataSource(postgresqlContainer.getJdbcUrl(), postgresqlContainer.getUsername(), postgresqlContainer.getPassword());
        var transactionRunner = new TransactionRunnerJdbc(dataSource);

        var dbExecutor = new DbExecutorImpl();
        EntityClassMetaData<Client> entityClassMetaDataClient = new EntityClassMetaDataImpl<>(Client.class);
        EntitySQLMetaData entitySQLMetaDataClient = new EntitySQLMetaDataImpl(entityClassMetaDataClient);
        var dataTemplateClient = new DataTemplateJdbc<>(dbExecutor, entityClassMetaDataClient, entitySQLMetaDataClient);

        this.serviceWithCache = new DbServiceClientWithCacheImpl(transactionRunner, dataTemplateClient);
        this.serviceWithoutCache = new DbServiceClientImpl(transactionRunner, dataTemplateClient);
    }

    @DisplayName("Создание \\ чтение записей")
    @ParameterizedTest(name = " с использованием cache: {0}")
    @ValueSource(booleans = {false, true})
    void doRun(boolean useCache) {
        logger.info("Use cache: {}", useCache);
        DBServiceClient dbServiceClient;
        if (useCache) {
            dbServiceClient = serviceWithCache;
        } else {
            dbServiceClient = serviceWithoutCache;
        }

        var startTime = System.currentTimeMillis();

        var ids = LongStream.range(1, 7000)
                .map(i -> dbServiceClient.saveClient(new Client(String.format("client[%s]", i))).getId())
                .toArray();
        var saveCompleteTime = System.currentTimeMillis();

        Arrays.stream(ids).forEach(dbServiceClient::getClient);
        var getCompleteTime = System.currentTimeMillis();

        logger.info("""
                        Time to save: {}
                        Time to get all by id: {}
                        Total time: {}""",
                saveCompleteTime - startTime,
                getCompleteTime - saveCompleteTime,
                getCompleteTime - startTime);
    }
}
