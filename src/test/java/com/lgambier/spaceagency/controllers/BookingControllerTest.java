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

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BookingControllerTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private JSONObject validMissionDataCreation;

    @BeforeAll
    public void setup() throws Exception {
        validMissionDataCreation = new JSONObject();
        validMissionDataCreation.put("shipId", 1);
        validMissionDataCreation.put("departureDate", LocalDateTime
                                                              .now().plusHours(2)
                                                              .toString());
        validMissionDataCreation.put("arrivalDate", LocalDateTime
                                                            .now()
                                                            .plusHours(5)
                                                            .toString());
        validMissionDataCreation.put("origin", "Mars");
        validMissionDataCreation.put("destination", "Neptune");
        validMissionDataCreation.put("status", MissionStatus.PLANNED);
        validMissionDataCreation.put("maxPassengers", 1);
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
    void createMission_withValidData_shouldReturn201() throws Exception {
        mockMvc
                .perform(post("/api/missions")
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .content(validMissionDataCreation.toString())
                                 .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(3)
    void addPassengerToMission_whenEverythingIsOk_shouldReturn200() throws Exception {
        JSONObject validPassengerDataCreation = new JSONObject();
        validPassengerDataCreation.put("firstName", "toto");
        validPassengerDataCreation.put("lastName", "tata");
        validPassengerDataCreation.put("email", "toto@tata.com");
        validPassengerDataCreation.put("weight", 75);
        validPassengerDataCreation.put("medicalClearance", true);

        mockMvc
                .perform(post("/api/passengers")
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .content(validPassengerDataCreation.toString())
                                 .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isCreated());




        JSONObject passengerBookingData = new JSONObject();
        passengerBookingData.put("passengerId", 1);

        mockMvc
                .perform(post("/api/bookings/1")
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .content(passengerBookingData.toString())
                                 .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isOk());

    }

    @Test
    @Order(4)
    void addPassengerToMission_whenMissionCapacityExceed_shouldReturn409() throws Exception {
        JSONObject validPassengerDataCreation = new JSONObject();
        validPassengerDataCreation.put("firstName", "toto");
        validPassengerDataCreation.put("lastName", "tata");
        validPassengerDataCreation.put("email", "passenger2@gmail.com");
        validPassengerDataCreation.put("weight", 75);
        validPassengerDataCreation.put("medicalClearance", true);


        mockMvc
                .perform(post("/api/passengers")
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .content(validPassengerDataCreation.toString())
                                 .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isCreated());


        JSONObject passengerBookingData = new JSONObject();
        passengerBookingData.put("passengerId", 2);

        mockMvc
                .perform(post("/api/bookings/1")
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .content(passengerBookingData.toString())
                                 .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isConflict());

    }

    @Test
    @Order(4)
    void addPassengerToMission_whenShipWeightExceed_shouldReturn409() throws Exception {
        JSONObject validPassengerDataCreation = new JSONObject();
        validPassengerDataCreation.put("firstName", "toto");
        validPassengerDataCreation.put("lastName", "tata");
        validPassengerDataCreation.put("email", "passenger3@gmail.com");
        validPassengerDataCreation.put("weight", 250);
        validPassengerDataCreation.put("medicalClearance", true);

        mockMvc
                .perform(post("/api/passengers")
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .content(validPassengerDataCreation.toString())
                                 .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isCreated());

        JSONObject updateMission = new JSONObject();
        updateMission.put("id", 1);
        updateMission.put("maxPassengers", 3);

        mockMvc
                .perform(patch("/api/missions")
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .content(updateMission.toString())
                                 .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isOk());


        JSONObject passengerBookingData = new JSONObject();
        passengerBookingData.put("passengerId", 3);

        mockMvc
                .perform(post("/api/bookings/1")
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .content(passengerBookingData.toString())
                                 .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isConflict());

    }
}
