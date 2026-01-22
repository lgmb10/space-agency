package com.lgambier.spaceagency.services;

import com.lgambier.spaceagency.dto.mission.MissionDTO;
import com.lgambier.spaceagency.dto.mission.request.MissionAddPassengerDTO;
import com.lgambier.spaceagency.dto.passenger.PassengerDTO;
import com.lgambier.spaceagency.dto.ship.ShipDTO;
import com.lgambier.spaceagency.enums.MissionStatus;
import com.lgambier.spaceagency.exceptions.mission.MissionPassengerAlreadyAffectedToGivenMissionException;
import com.lgambier.spaceagency.exceptions.mission.MissionShipCapacityExceedsException;
import com.lgambier.spaceagency.exceptions.mission.MissionShipWeightExceedsException;
import com.lgambier.spaceagency.exceptions.mission.MissionStatusInvalidToAddPassengerException;
import com.lgambier.spaceagency.exceptions.passenger.PassengerMedicalClearanceInvalidException;
import com.lgambier.spaceagency.models.Booking;
import com.lgambier.spaceagency.models.Passenger;
import com.lgambier.spaceagency.models.Ship;
import com.lgambier.spaceagency.repositories.BookingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

    @Mock
    BookingRepository bookingRepository;

    @Mock
    MissionService missionService;

    @InjectMocks
    BookingService bookingService;


    @Test
    void addPassenger_shouldAddPassenger_whenAllConditionsAreValid() {
        Integer missionId = 1;
        Integer passengerId = 2;

        MissionDTO mission = MissionDTO
                                     .builder()
                                     .id(missionId)
                                     .status(MissionStatus.PLANNED)
                                     .build();

        Passenger passenger = Passenger
                                         .builder()
                                         .id(passengerId)
                                         .weight(80)
                                         .medicalClearance(true)
                                         .build();

        Ship ship = Ship
                               .builder()
                               .id(10)
                               .maxWeight(1000)
                               .build();

        MissionAddPassengerDTO dto = new MissionAddPassengerDTO(passengerId);

        Booking savedBooking = Booking
                                       .builder()
                                       .missionId(missionId)
                                       .passengerId(passengerId)
                                       .build();

        when(missionService.findById(missionId)).thenReturn(mission);
        when(missionService.getTotalPassengersWeight(80, missionId)).thenReturn(300);
        when(missionService.isMissionShipCapacityReached(missionId)).thenReturn(false);
        when(bookingRepository.findByPassengerIdAndMissionId(passengerId, missionId)).thenReturn(Optional.empty());
        when(bookingRepository.save(any(Booking.class))).thenReturn(savedBooking);

        Booking result = bookingService.addPassenger(missionId, dto, ship, passenger);


        assertNotNull(result);
        assertEquals(passengerId, result.getPassengerId());
        assertEquals(missionId, result.getMissionId());

        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    void addPassenger_shouldThrowException_whenPassengerAlreadyAffected() {

        when(bookingRepository.findByPassengerIdAndMissionId(2, 1)).thenReturn(Optional.of(new Booking()));

        MissionAddPassengerDTO dto = new MissionAddPassengerDTO(2);

        assertThrows(MissionPassengerAlreadyAffectedToGivenMissionException.class,
                     () -> bookingService.addPassenger(1, dto, Ship
                                                                       .builder()
                                                                       .maxWeight(1000)
                                                                       .build(), Passenger
                                                                                         .builder()
                                                                                         .medicalClearance(true)
                                                                                         .weight(80)
                                                                                         .build()));

        verify(bookingRepository, never()).save(any());
    }

    @Test
    void addPassenger_shouldThrowException_whenMissionStatusIsNotPlanned() {

        when(missionService.findById(1)).thenReturn(MissionDTO
                                                            .builder()
                                                            .id(1)
                                                            .status(MissionStatus.IN_PROGRESS)
                                                            .build());

        MissionAddPassengerDTO dto = new MissionAddPassengerDTO(2);

        assertThrows(MissionStatusInvalidToAddPassengerException.class, () -> bookingService.addPassenger(1, dto,
                                                                                                          Ship
                                                                                                                  .builder()
                                                                                                                  .maxWeight(
                                                                                                                          1000)
                                                                                                                  .build(),
                                                                                                          Passenger
                                                                                                                  .builder()
                                                                                                                  .medicalClearance(
                                                                                                                          true)
                                                                                                                  .weight(80)
                                                                                                                  .build()));
    }

    @Test
    void addPassenger_shouldThrowException_whenMedicalClearanceIsInvalid() {

        when(missionService.findById(1)).thenReturn(MissionDTO
                                                            .builder()
                                                            .id(1)
                                                            .status(MissionStatus.PLANNED)
                                                            .build());

        Passenger passenger = Passenger
                                         .builder()
                                         .id(2)
                                         .medicalClearance(false)
                                         .build();

        MissionAddPassengerDTO dto = new MissionAddPassengerDTO(2);

        assertThrows(PassengerMedicalClearanceInvalidException.class, () -> bookingService.addPassenger(1, dto, Ship
                                                                                                                        .builder()
                                                                                                                        .maxWeight(
                                                                                                                                1000)
                                                                                                                        .build(),
                                                                                                        passenger));
    }

    @Test
    void addPassenger_shouldThrowException_whenShipCapacityIsReached() {

        when(missionService.findById(1)).thenReturn(MissionDTO
                                                            .builder()
                                                            .id(1)
                                                            .status(MissionStatus.PLANNED)
                                                            .build());

        when(missionService.isMissionShipCapacityReached(1)).thenReturn(true);

        MissionAddPassengerDTO dto = new MissionAddPassengerDTO(2);

        assertThrows(MissionShipCapacityExceedsException.class, () -> bookingService.addPassenger(1, dto, Ship
                                                                                                                  .builder()
                                                                                                                  .maxWeight(
                                                                                                                          1000)
                                                                                                                  .build(),
                                                                                                  Passenger
                                                                                                          .builder()
                                                                                                          .medicalClearance(
                                                                                                                  true)
                                                                                                          .weight(80)
                                                                                                          .build()));
    }

    @Test
    void addPassenger_shouldThrowException_whenShipWeightIsExceeded() {

        when(missionService.findById(1)).thenReturn(MissionDTO
                                                            .builder()
                                                            .id(1)
                                                            .status(MissionStatus.PLANNED)
                                                            .build());

        when(missionService.getTotalPassengersWeight(200, 1)).thenReturn(1200);

        Ship ship = Ship
                               .builder()
                               .maxWeight(1000)
                               .build();

        Passenger passenger = Passenger
                                         .builder()
                                         .id(2)
                                         .weight(200)
                                         .medicalClearance(true)
                                         .build();

        MissionAddPassengerDTO dto = new MissionAddPassengerDTO(2);

        assertThrows(MissionShipWeightExceedsException.class,
                     () -> bookingService.addPassenger(1, dto, ship, passenger));
    }
}
