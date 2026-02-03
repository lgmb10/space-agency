package com.lgambier.spaceagency.roles;

import com.lgambier.spaceagency.config.AbstractIntegrationTest;
import com.lgambier.spaceagency.controllers.AuthController;
import com.lgambier.spaceagency.dto.auth.AuthRequestDTO;
import com.lgambier.spaceagency.dto.mission.SanitizedMissionDTO;
import com.lgambier.spaceagency.models.Booking;
import com.lgambier.spaceagency.repositories.BookingRepository;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(properties = {"spring.flyway.locations=classpath:db/migration,classpath:db/migration-special-test"})
public class RoleAstronautTest extends AbstractIntegrationTest {

    protected String accessToken;

    @Autowired
    private AuthController authController;

    @Value("${TEST_ASTRONAUT_LOGIN}")
    private String login;

    @Value("${TEST_ASTRONAUT_PASSWORD}")
    private String password;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BookingRepository bookingRepository;


    @BeforeEach
    void obtainAccessToken() {
        accessToken = authController
                              .login(new AuthRequestDTO(login, password))
                              .getBody();
    }

    @Test
    void getAllShips_withRoleAstronaut_shouldReturn403() throws Exception {
        mockMvc
                .perform(get("/api/ships").header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isForbidden());
    }

    @Test
    void getAllPassengers_withRoleAstronaut_shouldReturn403() throws Exception {
        mockMvc
                .perform(get("/api/passengers").header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isForbidden());
    }

    @Order(1)
    @Test
    void addPassengerToMission_withRoleAstronaut_shouldReturn200() throws Exception {
        JSONObject passengerBookingData = new JSONObject();
        passengerBookingData.put("passengerId", 1);

        mockMvc
                .perform(post("/api/bookings/1")
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .content(passengerBookingData.toString())
                                 .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isOk());
    }

    @Order(2)
    @Test
    void getUserBookingMissions_withRoleAstronaut_shouldReturn200_withOnlyAssociatedBookingToUser() throws Exception {
        JSONObject passengerBookingData = new JSONObject();
        passengerBookingData.put("passengerId", 2);

        mockMvc
                .perform(post("/api/bookings/1")
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .content(passengerBookingData.toString())
                                 .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isOk());


        MvcResult result = mockMvc
                                   .perform(get("/api/missions/me").header(HttpHeaders.AUTHORIZATION,
                                                                           "Bearer " + accessToken))
                                   .andExpect(status().isOk())
                                   .andReturn();

        String responseBody = result
                                      .getResponse()
                                      .getContentAsString();

        List<SanitizedMissionDTO> missionDTOList = objectMapper.readValue(responseBody, objectMapper
                                                                                                .getTypeFactory()
                                                                                                .constructCollectionType(
                                                                                                        List.class,
                                                                                                        SanitizedMissionDTO.class));
        assertFalse(missionDTOList.isEmpty());

        List<Booking> astronautBookings = bookingRepository.findByPassengerId(1);
        assertFalse(astronautBookings.isEmpty());

        Set<Integer> missionIdsFromApi = missionDTOList
                                                 .stream()
                                                 .map(SanitizedMissionDTO::id)
                                                 .collect(Collectors.toSet());

        Set<Integer> missionIdsFromBookings = astronautBookings
                                                      .stream()
                                                      .map(Booking::getMissionId)
                                                      .collect(Collectors.toSet());


        assertTrue(missionIdsFromApi.containsAll(missionIdsFromBookings),
                   "A mission from the bookings is missing in the API response");

        assertTrue(missionIdsFromBookings.containsAll(missionIdsFromApi),
                   "A mission returned by the API does not belong to the astronaut's bookings");
    }
}
