package com.lgambier.spaceagency.config;


import com.lgambier.spaceagency.controllers.AuthController;
import com.lgambier.spaceagency.dto.auth.AuthRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@AutoConfigureMockMvc
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AbstractAuthenticatedIntegrationTest {

    protected String accessToken;

    @Autowired
    private AuthController authController;

    @Value("${TEST_ADMIN_LOGIN}")
    private String login;

    @Value("${TEST_ADMIN_PASSWORD}")
    private String password;


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

    static {
        MY_SQL_CONTAINER.start();
    }

    @BeforeEach
    void obtainAccessToken(){
        accessToken = authController
                              .login(new AuthRequestDTO(login, password))
                              .getBody();
    }

}
