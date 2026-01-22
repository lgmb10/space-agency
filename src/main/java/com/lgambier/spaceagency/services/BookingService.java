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
import com.lgambier.spaceagency.repositories.BookingRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;

    private final MissionService missionService;

    @Transactional
    public Booking addPassenger(Integer passengerId, Integer missionId){
        Booking booking = Booking.builder().passengerId(passengerId).missionId(missionId).build();

        return bookingRepository.save(booking);
    }

    public boolean isPassengerAlreadyAffectedToGivenMission(Integer passengerId, Integer missionId){
        return bookingRepository.findByPassengerIdAndMissionId(passengerId, missionId).isPresent();
    }

    @Transactional
    public Booking addPassenger(Integer missionId, MissionAddPassengerDTO passengerDTO, ShipDTO ship,
                                PassengerDTO passenger) {
        int passengerId = passengerDTO.getPassengerId();
        checkCanAddPassenger(missionId, passengerId, ship, passenger);

        return addPassenger(passengerId, missionId);
    }

    private void checkCanAddPassenger(Integer missionId, Integer passengerId, ShipDTO ship, PassengerDTO passenger) {
        MissionDTO mission = missionService.findById(missionId);
        int missionWeightWithNewPassenger = missionService.getTotalPassengersWeight(passenger.getWeight(), missionId);


        if (isPassengerAlreadyAffectedToGivenMission(passengerId, missionId)) {
            throw new MissionPassengerAlreadyAffectedToGivenMissionException(missionId);
        } else if (mission.getStatus() != MissionStatus.PLANNED) {
            throw new MissionStatusInvalidToAddPassengerException();
        } else if (!passenger.getMedicalClearance()) {
            throw new PassengerMedicalClearanceInvalidException();
        } else if (missionService.isMissionShipCapacityReached(missionId)) {
            throw new MissionShipCapacityExceedsException("Ship capacity is full, can't add more passenger",
                                                          HttpStatus.CONFLICT);
        } else if (missionWeightWithNewPassenger >= ship.getMaxWeight()) {
            throw new MissionShipWeightExceedsException();
        }
    }
}
