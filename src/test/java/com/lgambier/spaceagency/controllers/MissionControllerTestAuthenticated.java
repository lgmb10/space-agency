package com.lgambier.spaceagency.controllers;


import com.lgambier.spaceagency.config.AbstractAuthenticatedIntegrationTest;
import com.lgambier.spaceagency.enums.MissionStatus;
import com.lgambier.spaceagency.enums.ShipStatus;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MissionControllerTestAuthenticated extends AbstractAuthenticatedIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private JSONObject validMissionDataCreation;

    @BeforeAll
    public void setup() throws Exception {
        validMissionDataCreation = new JSONObject();
        validMissionDataCreation.put("shipId", 1);
        validMissionDataCreation.put("departureDate", LocalDateTime
                                                              .now()
                                                              .toString());
        validMissionDataCreation.put("arrivalDate", LocalDateTime
                                                            .now()
                                                            .plusHours(5)
                                                            .toString());
        validMissionDataCreation.put("origin", "Mars");
        validMissionDataCreation.put("destination", "Neptune");
        validMissionDataCreation.put("status", MissionStatus.PLANNED);
        validMissionDataCreation.put("maxPassengers", 2);
    }

    @Test
    void getAllMissions_shouldReturn200() throws Exception {
        mockMvc
                .perform(get("/api/missions").header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isOk());
    }

    @Test
    @Order(1)
    void createShipForMission_shouldReturn201() throws Exception {
        JSONObject ship = new JSONObject();
        ship.put("name", "ship");
        ship.put("capacity", 10);
        ship.put("status", ShipStatus.ACTIVE);
        ship.put("maxWeight", 300);


        mockMvc
                .perform(post("/api/ships")
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .content(ship.toString())
                                 .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(2)
    void createMission_withInvalidStatus_shouldReturn400() throws Exception {
        JSONObject invalidMission = new JSONObject(validMissionDataCreation.toString());
        invalidMission.put("status", "INVALID_STATUS");

        mockMvc
                .perform(post("/api/missions")
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .content(invalidMission.toString())
                                 .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(3)
    void createMission_withNotExistingShip_shouldReturn404() throws Exception {
        JSONObject invalidMission = new JSONObject(validMissionDataCreation.toString());
        invalidMission.put("shipId", 12345);

        mockMvc
                .perform(post("/api/missions")
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .content(invalidMission.toString())
                                 .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isNotFound());
    }


    @Test
    @Order(4)
    void createMission_withValidData_shouldReturn201() throws Exception {
        mockMvc
                .perform(post("/api/missions")
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .content(validMissionDataCreation.toString())
                                 .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(5)
    void createMission_withAlreadyInUseShip_shouldReturn409() throws Exception {
        mockMvc
                .perform(post("/api/missions")
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .content(validMissionDataCreation.toString())
                                 .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isConflict());
    }

    @Test
    @Order(6)
    void createMission_withRetiredShip_shouldReturn400() throws Exception {
        JSONObject ship = new JSONObject();
        ship.put("name", "ship-2");
        ship.put("capacity", 10);
        ship.put("status", ShipStatus.RETIRED);
        ship.put("maxWeight", 300);

        mockMvc
                .perform(post("/api/ships")
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .content(ship.toString())
                                 .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isCreated());

        JSONObject mission = new JSONObject(validMissionDataCreation.toString());
        mission.put("shipId", 2);

        mockMvc
                .perform(post("/api/missions")
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .content(mission.toString())
                                 .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(7)
    void createMission_withMaxPassengerSuperiorThanShipCapacity_shouldReturn409() throws Exception {
        JSONObject mission = new JSONObject(validMissionDataCreation.toString());
        mission.put("maxCapacity", 20);

        mockMvc
                .perform(post("/api/missions")
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .content(mission.toString())
                                 .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isConflict());
    }

    @Test
    @Order(8)
    void updateMissionStatus_fromPlannedToInProgress_shouldReturn200() throws Exception {
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

    @Test
    @Order(9)
    void updateMissionStatus_fromInProgressToCompleted_shouldReturn200() throws Exception {
        JSONObject status = new JSONObject();
        status.put("id", 1);
        status.put("status", "COMPLETED");

        mockMvc
                .perform(patch("/api/missions/status")
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .content(status.toString())
                                 .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isOk());
    }

    @Test
    @Order(10)
    void updateMissionStatus_fromCompletedToPlanned_shouldReturn409() throws Exception {
        JSONObject status = new JSONObject();
        status.put("id", 1);
        status.put("status", "PLANNED");

        mockMvc
                .perform(patch("/api/missions/status")
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .content(status.toString())
                                 .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isConflict());
    }
}
