package com.lgambier.spaceagency.services;

import com.lgambier.spaceagency.dto.mission.request.MissionCreateRequestDTO;
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

    @Mock
    ShipService shipService;

    MissionService missionService;

    @BeforeEach
    void setUp() {
        missionService = new MissionService(missionRepository, shipService);
    }

    @Test
    void createMission_shouldSaveMission_whenDataIsValid() {
        Ship ship = Ship.builder()
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
                .ship(ship)
                .maxPassengers(request.getMaxPassengers())
                .departureDate(departure)
                .arrivalDate(arrival)
                .origin(request.getOrigin())
                .destination(request.getDestination())
                .status(request.getStatus())
                .build();

        when(shipService.findById(ship.getId())).thenReturn(ship);
        when(missionRepository.existsOverlappingMission(ship.getId(), departure, arrival))
                .thenReturn(false);
        when(missionRepository.save(any(Mission.class)))
                .thenReturn(expectedMission);

        Mission savedMission = missionService.create(request);

        assertNotNull(savedMission, "The saved mission should not be null");
        assertEquals(expectedMission.getShip(), savedMission.getShip());
        assertEquals(expectedMission.getMaxPassengers(), savedMission.getMaxPassengers());
        assertEquals(expectedMission.getDepartureDate(), savedMission.getDepartureDate());
        assertEquals(expectedMission.getArrivalDate(), savedMission.getArrivalDate());
        assertEquals(expectedMission.getOrigin(), savedMission.getOrigin());
        assertEquals(expectedMission.getDestination(), savedMission.getDestination());
        assertEquals(expectedMission.getStatus(), savedMission.getStatus());

        verify(shipService).findById(ship.getId());
        verify(missionRepository).existsOverlappingMission(ship.getId(), departure, arrival);
        verify(missionRepository).save(any(Mission.class));
    }

    @Test
    void createMission_shouldThrowException_whenMaxPassengersExceedsShipCapacity() {
        Ship ship = Ship.builder()
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

        when(shipService.findById(ship.getId()))
                .thenReturn(ship);

        assertThrows(
                MissionShipCapacityExceeds.class,
                () -> missionService.create(request)
        );

        verify(missionRepository, never()).save(any());
        verify(missionRepository, never())
                .existsOverlappingMission(anyInt(), any(), any());
    }

    @Test
    void createMission_shouldThrowException_whenDatesOverlapForShip() {
        Ship ship = Ship.builder()
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

        when(shipService.findById(ship.getId()))
                .thenReturn(ship);

        when(missionRepository.existsOverlappingMission(
                ship.getId(),
                departure,
                arrival
        )).thenReturn(true); // 🔥 déclenche checkDatesOverlap

        assertThrows(
                MissionShipTimeSlotAlreadyInUse.class,
                () -> missionService.create(request)
        );

        verify(missionRepository, never()).save(any());
    }
}
