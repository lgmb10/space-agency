package com.lgambier.spaceagency.controllers;


import com.lgambier.spaceagency.config.AbstractIntegrationTest;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PassengerControllerTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private JSONObject validPassengerDataCreation;

    @BeforeEach
    public void setup() throws Exception {
        validPassengerDataCreation = new JSONObject();
        validPassengerDataCreation.put("firstName", "toto");
        validPassengerDataCreation.put("lastName", "tata");
        validPassengerDataCreation.put("email", "toto@tata.com");
        validPassengerDataCreation.put("weight", 75);
        validPassengerDataCreation.put("medicalClearance", true);
    }

    @Test
    void getAllPassengers_shouldReturn200() throws Exception {
        mockMvc
                .perform(get("/api/passengers").header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isOk());
    }

    @Test
    void createPassenger_withValidData_shouldReturn201() throws Exception {
        mockMvc
                .perform(post("/api/passengers")
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .content(validPassengerDataCreation.toString())
                                 .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isCreated());
    }

    @Test
    void createPassenger_withNegativeWeight_shouldReturn400() throws Exception {
        JSONObject invalidPassenger = validPassengerDataCreation;
        invalidPassenger.put("weight", -60);

        mockMvc
                .perform(post("/api/passengers")
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .content(invalidPassenger.toString())
                                 .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createPassenger_withInvalidEmail_shouldReturn400() throws Exception {
        JSONObject invalidPassenger = validPassengerDataCreation;
        invalidPassenger.put("email", "invalidemail.com");

        mockMvc
                .perform(post("/api/passengers")
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .content(invalidPassenger.toString())
                                 .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isBadRequest());
    }

}
