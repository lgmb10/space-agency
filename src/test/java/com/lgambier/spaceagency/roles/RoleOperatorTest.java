package com.lgambier.spaceagency.roles;

import com.lgambier.spaceagency.config.AbstractIntegrationTest;
import com.lgambier.spaceagency.controllers.AuthController;
import com.lgambier.spaceagency.dto.auth.AuthRequestDTO;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(properties = {"spring.flyway.locations=classpath:db/migration,classpath:db/migration-special-test"})
public class RoleOperatorTest extends AbstractIntegrationTest {

    protected String accessToken;

    @Autowired
    private AuthController authController;

    @Value("${TEST_OPERATOR_LOGIN}")
    private String login;

    @Value("${TEST_OPERATOR_PASSWORD}")
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
    void getAllShips_withRolePlanner_shouldReturn403() throws Exception {
        mockMvc
                .perform(get("/api/ships").header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isForbidden());
    }

    @Test
    void updateMissionStatus_withRolePlanner_fromPannedToInProgress_shouldReturn200() throws Exception {
        JSONObject status = new JSONObject();
        status.put("id", 1);
        status.put("status", "IN_PROGRESS");

        mockMvc
                .perform(patch("/api/missions/status")
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .content(status.toString())
                                 .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isOk());
    }
}
