package com.lgambier.spaceagency.controllers;


import com.lgambier.spaceagency.config.AbstractIntegrationTest;
import com.lgambier.spaceagency.enums.ShipStatus;
import com.lgambier.spaceagency.models.Ship;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ShipControllerTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private Ship validTestShip;

    private JSONObject validShipDataCreation;

    @BeforeEach
    public void setup() throws Exception {
        validTestShip = new Ship();
        validTestShip.setCapacity(10);
        validTestShip.setName("ship");
        validTestShip.setStatus(ShipStatus.RETIRED);
        validTestShip.setMaxWeight(300);

        validShipDataCreation = new JSONObject();
        validShipDataCreation.put("name", validTestShip.getName());
        validShipDataCreation.put("capacity", validTestShip.getCapacity());
        validShipDataCreation.put("status", validTestShip.getStatus());
        validShipDataCreation.put("maxWeight", validTestShip.getMaxWeight());
    }

    @Test
    void getMissions_shouldReturn200() throws Exception {
        mockMvc
                .perform(get("/api/ships").header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isOk());
    }


    @Test
    void createMission_withValidData_shouldReturn201() throws Exception {
        mockMvc
                .perform(post("/api/ships")
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .content(validShipDataCreation.toString())
                                 .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isCreated());
    }

    @Test
    void createMission_withInvalidData_shouldReturn400() throws Exception {
        JSONObject invalidShip = validShipDataCreation;
        invalidShip.put("capacity", "string");

        mockMvc
                .perform(post("/api/ships")
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .content(invalidShip.toString())
                                 .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createMission_withInvalidStatus_shouldReturn400() throws Exception {
        JSONObject invalidShip = validShipDataCreation;
        invalidShip.put("status", "INVALID_STATUS");

        mockMvc
                .perform(post("/api/ships")
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .content(invalidShip.toString())
                                 .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isBadRequest());
    }


}
