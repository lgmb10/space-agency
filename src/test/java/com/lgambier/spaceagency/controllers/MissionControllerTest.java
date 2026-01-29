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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MissionControllerTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private JSONObject validMissionDataCreation;

    @BeforeAll
    public void setup() throws Exception {
        validMissionDataCreation = new JSONObject();
        validMissionDataCreation.put("shipId", 1);
        validMissionDataCreation.put("departureDate", "2026-01-19T12:42:00");
        validMissionDataCreation.put("arrivalDate", "2026-01-19T20:42:00");
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
    void createMission_withNotExistingShip_shouldReturn404() throws Exception {
        JSONObject invalidMission = validMissionDataCreation;
        invalidMission.put("shipId", 12345);

        mockMvc
                .perform(post("/api/missions")
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .content(validMissionDataCreation.toString())
                                 .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isNotFound());
    }


    @Test
    @Order(1)
    void createMission_withValidData_shouldReturn201() throws Exception {
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
        
        mockMvc
                .perform(post("/api/missions")
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .content(validMissionDataCreation.toString())
                                 .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(2)
    void createMission_withAlreadyInUseShip_shouldReturn409() throws Exception {
        mockMvc
                .perform(post("/api/missions")
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .content(validMissionDataCreation.toString())
                                 .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isConflict());
    }

    @Test
    @Order(3)
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
    @Order(4)
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

//
//    @Test
//    @Order(2)
//    void getCreatedMission_shouldReturn200() throws Exception {
//        mockMvc
//                .perform(get("/api/passengers/1").header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @Order(3)
//    void createMission_withExitingMissionWithSameEmail_shouldReturn409() throws Exception {
//        mockMvc
//                .perform(post("/api/passengers")
//                                 .contentType(MediaType.APPLICATION_JSON)
//                                 .content(validMissionDataCreation.toString())
//                                 .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
//                .andExpect(status().isConflict());
//    }
//
//
//    @Test
//    void createMission_withNegativeWeight_shouldReturn400() throws Exception {
//        JSONObject invalidMission = validMissionDataCreation;
//        invalidMission.put("weight", -60);
//
//        mockMvc
//                .perform(post("/api/passengers")
//                                 .contentType(MediaType.APPLICATION_JSON)
//                                 .content(invalidMission.toString())
//                                 .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    void createMission_withInvalidEmail_shouldReturn400() throws Exception {
//        JSONObject invalidMission = validMissionDataCreation;
//        invalidMission.put("email", "invalidemail.com");
//
//        mockMvc
//                .perform(post("/api/passengers")
//                                 .contentType(MediaType.APPLICATION_JSON)
//                                 .content(invalidMission.toString())
//                                 .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
//                .andExpect(status().isBadRequest());
//    }

}
