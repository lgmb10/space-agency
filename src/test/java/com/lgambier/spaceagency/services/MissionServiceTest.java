package com.lgambier.spaceagency.services;

import com.lgambier.spaceagency.dto.mission.MissionDTO;
import com.lgambier.spaceagency.dto.mission.request.MissionCreateRequestDTO;
import com.lgambier.spaceagency.dto.ship.ShipDTO;
import com.lgambier.spaceagency.enums.MissionStatus;
import com.lgambier.spaceagency.exceptions.mission.MissionShipCapacityExceeds;
import com.lgambier.spaceagency.exceptions.mission.MissionShipTimeSlotAlreadyInUse;
import com.lgambier.spaceagency.models.Mission;
import com.lgambier.spaceagency.models.Ship;
import com.lgambier.spaceagency.repositories.MissionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MissionServiceTest {

    @Mock
    MissionRepository missionRepository;

    MissionService missionService;

    @BeforeEach
    void setUp() {
        missionService = new MissionService(missionRepository);
    }

    @Test
    void createMission_shouldSaveMission_whenDataIsValid() {
        ShipDTO ship = ShipDTO.builder()
                              .id(1)
                              .capacity(10)
                              .build();

        LocalDateTime departure = LocalDateTime.now().plusHours(1);
        LocalDateTime arrival = LocalDateTime.now().plusHours(3);

        MissionCreateRequestDTO request = MissionCreateRequestDTO.builder()
                .shipId(ship.getId())
                .maxPassengers(5)
                .departureDate(departure)
                .arrivalDate(arrival)
                .origin("Earth")
                .destination("Mars")
                .status(MissionStatus.PLANNED)
                .build();

        Mission expectedMission = Mission.builder()
                .ship(ShipDTO.toShip(ship))
                .maxPassengers(request.getMaxPassengers())
                .departureDate(departure)
                .arrivalDate(arrival)
                .origin(request.getOrigin())
                .destination(request.getDestination())
                .status(request.getStatus())
                .build();

        when(missionRepository.existsOverlappingMission(ship.getId(), departure, arrival))
                .thenReturn(false);
        when(missionRepository.save(any(Mission.class)))
                .thenReturn(expectedMission);

        MissionDTO savedMission = missionService.create(request, ShipDTO.toShip(ship));

        assertNotNull(savedMission, "The saved mission should not be null");
        assertEquals(expectedMission.getShip(), savedMission.getShip());
        assertEquals(expectedMission.getMaxPassengers(), savedMission.getMaxPassengers());
        assertEquals(expectedMission.getDepartureDate(), savedMission.getDepartureDate());
        assertEquals(expectedMission.getArrivalDate(), savedMission.getArrivalDate());
        assertEquals(expectedMission.getOrigin(), savedMission.getOrigin());
        assertEquals(expectedMission.getDestination(), savedMission.getDestination());
        assertEquals(expectedMission.getStatus(), savedMission.getStatus());

        verify(missionRepository).existsOverlappingMission(ship.getId(), departure, arrival);
        verify(missionRepository).save(any(Mission.class));
    }

    @Test
    void createMission_shouldThrowException_whenMaxPassengersExceedsShipCapacity() {
        ShipDTO ship = ShipDTO.builder()
                           .id(1)
                           .capacity(10)
                           .build();

        MissionCreateRequestDTO request = MissionCreateRequestDTO.builder()
                .shipId(ship.getId())
                .maxPassengers(15)
                .departureDate(LocalDateTime.now().plusHours(1))
                .arrivalDate(LocalDateTime.now().plusHours(2))
                .origin("Earth")
                .destination("Mars")
                .status(MissionStatus.PLANNED)
                .build();


        assertThrows(
                MissionShipCapacityExceeds.class,
                () -> missionService.create(request, ShipDTO.toShip(ship))
        );

        verify(missionRepository, never()).save(any());
        verify(missionRepository, never())
                .existsOverlappingMission(anyInt(), any(), any());
    }

    @Test
    void createMission_shouldThrowException_whenDatesOverlapForShip() {
        ShipDTO ship = ShipDTO.builder()
                              .id(1)
                              .capacity(10)
                              .build();

        LocalDateTime departure = LocalDateTime.now().plusHours(1);
        LocalDateTime arrival = LocalDateTime.now().plusHours(3);

        MissionCreateRequestDTO request = MissionCreateRequestDTO.builder()
                .shipId(ship.getId())
                .maxPassengers(5)
                .departureDate(departure)
                .arrivalDate(arrival)
                .origin("Earth")
                .destination("Mars")
                .status(MissionStatus.PLANNED)
                .build();

        when(missionRepository.existsOverlappingMission(
                ship.getId(),
                departure,
                arrival
        )).thenReturn(true);

        assertThrows(
                MissionShipTimeSlotAlreadyInUse.class,
                () -> missionService.create(request, ShipDTO.toShip(ship))
        );

        verify(missionRepository, never()).save(any());
    }
}
