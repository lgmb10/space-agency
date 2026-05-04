package com.lgambier.spaceagency.actuator;

import com.lgambier.spaceagency.config.AbstractIntegrationTest;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ActuatorTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void accessToActuatorHealth_statusMustBeUp() throws Exception {
        MvcResult result = mockMvc
                                   .perform(get("/actuator/health"))
                                   .andExpect(status().isOk())
                                   .andReturn();

        String content = result
                                 .getResponse()
                                 .getContentAsString();
        JSONObject json = new JSONObject(content);
        assertEquals("UP", json.getString("status"));
    }
}
