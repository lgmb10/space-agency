package com.lgambier.spaceagency.config;


import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@AutoConfigureMockMvc
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class AbstractIntegrationTest {

    private static final DockerImageName MYSQL_IMAGE = DockerImageName
                                                               .parse("mysql:8.4")
                                                               .asCompatibleSubstituteFor("mysql");

    @Container
    private static final MySQLContainer<?> MY_SQL_CONTAINER = new MySQLContainer(MYSQL_IMAGE)
                                                                      .withDatabaseName("spaceagency")
                                                                      .withUsername("root")
                                                                      .withPassword("root");

    @DynamicPropertySource
    static void registerProps(DynamicPropertyRegistry registry) {
        if (!MY_SQL_CONTAINER.isRunning()) {
            MY_SQL_CONTAINER.start();
        }
        registry.add("spring.datasource.url", MY_SQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", MY_SQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", MY_SQL_CONTAINER::getPassword);
    }

}
