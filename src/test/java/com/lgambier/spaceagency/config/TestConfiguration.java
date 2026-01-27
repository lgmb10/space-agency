package com.lgambier.spaceagency.config;


import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestConfiguration {

    private static final DockerImageName MYSQL_IMAGE = DockerImageName
                                                               .parse("mysql:8.4")
                                                               .asCompatibleSubstituteFor("mysql");

    @Container
    private static final MySQLContainer<?> MY_SQL_CONTAINER = new MySQLContainer(MYSQL_IMAGE)
                                                                      .withDatabaseName("spaceagency")
                                                                      .withUsername("mysql")
                                                                      .withPassword("mysql");


    @DynamicPropertySource
    static void registerProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", MY_SQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", MY_SQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", MY_SQL_CONTAINER::getPassword);
    }

//    @Test
//    void test() {
//        assertThat(MY_SQL_CONTAINER.isRunning()).isTrue();
//    }


}
