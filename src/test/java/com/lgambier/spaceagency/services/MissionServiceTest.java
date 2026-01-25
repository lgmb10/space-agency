package com.lgambier.spaceagency.services;

import com.lgambier.spaceagency.dto.mappers.ShipMapper;
import com.lgambier.spaceagency.dto.mission.MissionDTO;
import com.lgambier.spaceagency.dto.mission.request.MissionCreateRequestDTO;
import com.lgambier.spaceagency.dto.mission.request.MissionUpdateStatusRequestDTO;
import com.lgambier.spaceagency.dto.ship.ShipDTO;
import com.lgambier.spaceagency.enums.MissionStatus;
import com.lgambier.spaceagency.exceptions.mission.MissionShipCapacityExceedsException;
import com.lgambier.spaceagency.exceptions.mission.MissionShipTimeSlotAlreadyInUseException;
import com.lgambier.spaceagency.exceptions.mission.MissionTransitionException;
import com.lgambier.spaceagency.models.Mission;
import com.lgambier.spaceagency.repositories.MissionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MissionServiceTest {

    @Mock
    MissionRepository missionRepository;

    @InjectMocks
    MissionService missionService;


    @Test
    void createMission_shouldSaveMission_whenDataIsValid() {
        ShipDTO ship = ShipDTO
                               .builder()
                               .id(1)
                               .capacity(10)
                               .build();

        LocalDateTime departure = LocalDateTime
                                          .now()
                                          .plusHours(1);
        LocalDateTime arrival = LocalDateTime
                                        .now()
                                        .plusHours(3);

        MissionCreateRequestDTO request = MissionCreateRequestDTO
                                                  .builder()
                                                  .shipId(ship.getId())
                                                  .maxPassengers(5)
                                                  .departureDate(departure)
                                                  .arrivalDate(arrival)
                                                  .origin("Earth")
                                                  .destination("Mars")
                                                  .status(MissionStatus.PLANNED)
                                                  .build();

        Mission expectedMission = Mission
                                          .builder()
                                          .ship(ShipMapper.INSTANCE
                                                        .shipDtotoShip(ship))
                                          .maxPassengers(request.getMaxPassengers())
                                          .departureDate(departure)
                                          .arrivalDate(arrival)
                                          .origin(request.getOrigin())
                                          .destination(request.getDestination())
                                          .status(request.getStatus())
                                          .build();

        when(missionRepository.existsOverlappingMission(ship.getId(), departure, arrival)).thenReturn(false);
        when(missionRepository.save(any(Mission.class))).thenReturn(expectedMission);

        MissionDTO savedMission = missionService.create(request, ShipMapper.INSTANCE
                                                                         .shipDtotoShip(ship));

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
        ShipDTO ship = ShipDTO
                               .builder()
                               .id(1)
                               .capacity(10)
                               .build();

        MissionCreateRequestDTO request = MissionCreateRequestDTO
                                                  .builder()
                                                  .shipId(ship.getId())
                                                  .maxPassengers(15)
                                                  .departureDate(LocalDateTime
                                                                         .now()
                                                                         .plusHours(1))
                                                  .arrivalDate(LocalDateTime
                                                                       .now()
                                                                       .plusHours(2))
                                                  .origin("Earth")
                                                  .destination("Mars")
                                                  .status(MissionStatus.PLANNED)
                                                  .build();


        assertThrows(MissionShipCapacityExceedsException.class,
                     () -> missionService.create(request, ShipMapper.INSTANCE
                                                                  .shipDtotoShip(ship)));

        verify(missionRepository, never()).save(any());
        verify(missionRepository, never()).existsOverlappingMission(anyInt(), any(), any());
    }

    @Test
    void createMission_shouldThrowException_whenDatesOverlapForShip() {
        ShipDTO ship = ShipDTO
                               .builder()
                               .id(1)
                               .capacity(10)
                               .build();

        LocalDateTime departure = LocalDateTime
                                          .now()
                                          .plusHours(1);
        LocalDateTime arrival = LocalDateTime
                                        .now()
                                        .plusHours(3);

        MissionCreateRequestDTO request = MissionCreateRequestDTO
                                                  .builder()
                                                  .shipId(ship.getId())
                                                  .maxPassengers(5)
                                                  .departureDate(departure)
                                                  .arrivalDate(arrival)
                                                  .origin("Earth")
                                                  .destination("Mars")
                                                  .status(MissionStatus.PLANNED)
                                                  .build();

        when(missionRepository.existsOverlappingMission(ship.getId(), departure, arrival)).thenReturn(true);

        assertThrows(MissionShipTimeSlotAlreadyInUseException.class,
                     () -> missionService.create(request, ShipMapper.INSTANCE
                                                                  .shipDtotoShip(ship)));

        verify(missionRepository, never()).save(any());
    }

    @Test
    void patchStatus_shouldUpdateStatus_whenTransitionIsValid() {
        LocalDateTime departure = LocalDateTime
                                          .now()
                                          .minusHours(1);

        Mission mission = Mission
                                  .builder()
                                  .id(1)
                                  .status(MissionStatus.PLANNED)
                                  .departureDate(departure)
                                  .build();

        MissionUpdateStatusRequestDTO request = new MissionUpdateStatusRequestDTO();
        request.setId(mission.getId());
        request.setStatus(MissionStatus.IN_PROGRESS);

        when(missionRepository.findById(mission.getId())).thenReturn(Optional.of(mission));

        when(missionRepository.save(any(Mission.class))).thenAnswer(invocation -> invocation.getArgument(0));

        MissionDTO updated = missionService.patchStatus(request);

        assertEquals(MissionStatus.IN_PROGRESS, updated.getStatus());

        ArgumentCaptor<Mission> captor = ArgumentCaptor.forClass(Mission.class);
        verify(missionRepository).save(captor.capture());

        Mission savedMission = captor.getValue();
        assertEquals(MissionStatus.IN_PROGRESS, savedMission.getStatus());
        assertEquals(mission.getId(), savedMission.getId());
    }

    @Test
    void patchStatus_shouldThrowException_whenTransitionInvalid() {
        Mission mission = Mission
                                  .builder()
                                  .id(1)
                                  .status(MissionStatus.PLANNED)
                                  .departureDate(LocalDateTime.now())
                                  .build();

        MissionUpdateStatusRequestDTO request = new MissionUpdateStatusRequestDTO();
        request.setId(mission.getId());
        request.setStatus(MissionStatus.COMPLETED);

        when(missionRepository.findById(mission.getId())).thenReturn(Optional.of(mission));

        assertThrows(MissionTransitionException.class, () -> missionService.patchStatus(request));
        verify(missionRepository, never()).save(any());
    }

    @Test
    void patchStatus_shouldThrowException_whenInProgressBeforeDeparture() {
        LocalDateTime futureDeparture = LocalDateTime
                                                .now()
                                                .plusHours(2);
        Mission mission = Mission
                                  .builder()
                                  .id(1)
                                  .status(MissionStatus.PLANNED)
                                  .departureDate(futureDeparture)
                                  .build();

        MissionUpdateStatusRequestDTO request = new MissionUpdateStatusRequestDTO();
        request.setId(mission.getId());
        request.setStatus(MissionStatus.IN_PROGRESS);

        when(missionRepository.findById(mission.getId())).thenReturn(Optional.of(mission));

        MissionTransitionException ex = assertThrows(MissionTransitionException.class,
                                                     () -> missionService.patchStatus(request));

        assertTrue(ex
                           .getMessage()
                           .contains("departure date is in the future"));
    }


}
