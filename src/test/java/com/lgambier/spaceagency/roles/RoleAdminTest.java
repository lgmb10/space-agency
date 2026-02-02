package com.lgambier.spaceagency.roles;

import com.lgambier.spaceagency.config.AbstractIntegrationTest;
import com.lgambier.spaceagency.controllers.AuthController;
import com.lgambier.spaceagency.dto.auth.AuthRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RoleAdminTest extends AbstractIntegrationTest {

    protected String accessToken;

    @Autowired
    private AuthController authController;

    @Value("${TEST_ADMIN_LOGIN}")
    private String login;

    @Value("${TEST_ADMIN_PASSWORD}")
    private String password;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void obtainAccessToken() {
        accessToken = authController
                              .login(new AuthRequestDTO(login, password))
                              .getBody();
    }

    @Test
    void getAllShips_withRoleAdmin_shouldReturn200() throws Exception {
        mockMvc
                .perform(get("/api/ships").header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isOk());
    }

}
