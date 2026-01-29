package com.lgambier.spaceagency.controllers;


import com.lgambier.spaceagency.config.AbstractIntegrationTest;
import com.lgambier.spaceagency.enums.MissionStatus;
import com.lgambier.spaceagency.enums.ShipStatus;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ShipControllerTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private JSONObject validShipDataCreation;

    @BeforeEach
    public void setup() throws Exception {
        validShipDataCreation = new JSONObject();
        validShipDataCreation.put("name", "ship");
        validShipDataCreation.put("capacity", 10);
        validShipDataCreation.put("status", ShipStatus.ACTIVE);
        validShipDataCreation.put("maxWeight", 300);
    }

    @Test
    void getAllShips_shouldReturn200() throws Exception {
        mockMvc
                .perform(get("/api/ships").header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isOk());
    }


    @Test
    @Order(1)
    void createShip_withValidData_shouldReturn201() throws Exception {
        mockMvc
                .perform(post("/api/ships")
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .content(validShipDataCreation.toString())
                                 .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(2)
    void getCreatedShip_shouldReturn200() throws Exception {
        mockMvc
                .perform(get("/api/ships/1").header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isOk());
    }

    @Test
    @Order(3)
    void deleteShip_associatedToPlannedMission_shouldReturn409() throws Exception {
        JSONObject mission = new JSONObject();
        // Associated with previously created ship
        mission.put("shipId", 1);
        mission.put("departureDate", "2026-01-19T12:42:00");
        mission.put("arrivalDate", "2026-01-19T20:42:00");
        mission.put("origin", "Jupiter");
        mission.put("destination", "Marseile");
        mission.put("status", MissionStatus.PLANNED);
        mission.put("maxPassengers", 2);

        mockMvc
                .perform(post("/api/missions")
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .content(mission.toString())
                                 .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isCreated());

        mockMvc
                .perform(delete("/api/ships/1").header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isConflict());
    }

    @Test
    void createShip_withInvalidData_shouldReturn400() throws Exception {
        JSONObject invalidShip = new JSONObject(validShipDataCreation.toString());
        invalidShip.put("capacity", "string");

        mockMvc
                .perform(post("/api/ships")
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .content(invalidShip.toString())
                                 .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createShip_withInvalidStatus_shouldReturn400() throws Exception {
        JSONObject invalidShip = new JSONObject(validShipDataCreation.toString());
        invalidShip.put("status", "INVALID_STATUS");

        mockMvc
                .perform(post("/api/ships")
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .content(invalidShip.toString())
                                 .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isBadRequest());
    }
}
