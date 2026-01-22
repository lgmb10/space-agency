package com.lgambier.spaceagency.services;

import com.lgambier.spaceagency.dto.mission.MissionDTO;
import com.lgambier.spaceagency.dto.mission.request.*;
import com.lgambier.spaceagency.dto.passenger.PassengerDTO;
import com.lgambier.spaceagency.dto.ship.ShipDTO;
import com.lgambier.spaceagency.enums.MissionStatus;
import com.lgambier.spaceagency.exceptions.mission.*;
import com.lgambier.spaceagency.exceptions.passenger.PassengerMedicalClearanceInvalidException;
import com.lgambier.spaceagency.models.Booking;
import com.lgambier.spaceagency.models.Mission;
import com.lgambier.spaceagency.models.Ship;
import com.lgambier.spaceagency.repositories.MissionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import tools.jackson.databind.json.JsonMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MissionService {

    private final MissionRepository missionRepository;

    private final BookingService bookingService;

    private final JsonMapper jsonMapper;

    public List<MissionDTO> findAll() {
        List<Mission> missions = missionRepository.findAll();
        return missions
                       .stream()
                       .map(MissionDTO::toDTO)
                       .collect(Collectors.toList());
    }

    public MissionDTO findById(int id) {
        Mission mission = missionRepository
                                  .findById(id)
                                  .orElseThrow(() -> new MissionNotFoundException(id));

        return MissionDTO.toDTO(mission);
    }

    public List<MissionDTO> findAvailableMissions() {
        List<Mission> missions = missionRepository.findAvailableMissions();

        return missions
                       .stream()
                       .map(MissionDTO::toDTO)
                       .collect(Collectors.toList());
    }

    @Transactional
    public MissionDTO create(MissionCreateRequestDTO missionRequest, Ship ship) {

        checkCapacity(missionRequest.getMaxPassengers(), ship);

        Mission mission = MissionCreateRequestDTO.toMission(missionRequest, ship);
        checkDatesOverlap(ship.getId(), mission, false);

        return MissionDTO.toDTO(missionRepository.save(mission));
    }

    @Transactional
    public MissionDTO update(MissionUpdateRequestDTO missionRequest, Ship ship) {
        findById(missionRequest.getId());

        checkCapacity(missionRequest.getMaxPassengers(), ship);

        Mission mission = MissionUpdateRequestDTO.toMission(missionRequest, ship);
        checkDatesOverlap(ship.getId(), mission, true);

        return MissionDTO.toDTO(missionRepository.save(mission));
    }

    @Transactional
    public MissionDTO patch(MissionPatchRequestDTO missionRequest, Ship ship) {
        Mission mission = MissionDTO.toMission(findById(missionRequest.getId()));
        Mission patchedMission = createMissionFromPatchDTO(missionRequest, mission, ship, true);

        return MissionDTO.toDTO(missionRepository.save(patchedMission));
    }

    @Transactional
    public MissionDTO patchStatus(MissionUpdateStatusRequestDTO missionRequest) {
        Mission mission = MissionDTO.toMission(findById(missionRequest.getId()));
        MissionStatus oldStatus = mission.getStatus();
        MissionStatus newStatus = missionRequest.getStatus();
        boolean hasToThrow = false;

        switch (newStatus) {
            case PLANNED -> {
                if (oldStatus != MissionStatus.PLANNED) hasToThrow = true;
            }
            case COMPLETED -> {
                if (oldStatus != MissionStatus.IN_PROGRESS) hasToThrow = true;
            }
            case IN_PROGRESS -> {
                if (oldStatus != MissionStatus.PLANNED) hasToThrow = true;
                else if (mission
                                 .getDepartureDate()
                                 .isAfter(LocalDateTime.now())) {
                    throw new MissionTransitionException(oldStatus, newStatus,
                                                         "You cannot set this mission status to PLANNED because the departure date is in the future.");
                }
            }
        }

        if (hasToThrow) throw new MissionTransitionException(oldStatus, newStatus);

        mission.setStatus(missionRequest.getStatus());
        return MissionDTO.toDTO(missionRepository.save(mission));
    }

    @Transactional
    public void deleteById(Integer id) {
        findById(id);
        missionRepository.deleteById(id);
    }


    @Transactional
    public Booking addPassenger(Integer missionId, MissionAddPassengerDTO passengerDTO, ShipDTO ship,
                                PassengerDTO passenger) {
        int passengerId = passengerDTO.getPassengerId();
        checkCanAddPassenger(missionId, passengerId, ship, passenger);

        return bookingService.addPassenger(passengerId, missionId);
    }

    private void checkCapacity(Integer maxPassengers, Ship ship) {
        if (maxPassengers != null && maxPassengers > ship.getCapacity()) {
            throw new MissionShipCapacityExceedsException(maxPassengers, ship);
        }
    }

    private void checkDatesOverlap(Integer shipId, Mission mission, Boolean onUpdate) {
        if (missionRepository.existsOverlappingMission(shipId, mission.getDepartureDate(), mission.getArrivalDate())) {
            if (onUpdate) throw new MissionShipTimeSlotAlreadyInUseException().onUpdate();
            else throw new MissionShipTimeSlotAlreadyInUseException();
        }
    }

    private Mission createMissionFromPatchDTO(MissionPatchRequestDTO missionRequest, Mission currentMission, Ship ship,
                                              Boolean onUpdate) {
        Mission patchedMission = jsonMapper.updateValue(currentMission, missionRequest);

        if (missionRequest.getShipId() != null) {
            checkCapacity(missionRequest.getMaxPassengers(), ship);
            patchedMission.setShip(ship);
            checkDatesOverlap(ship.getId(), currentMission, onUpdate);
        }

        return patchedMission;
    }

    private void checkCanAddPassenger(Integer missionId, Integer passengerId, ShipDTO ship, PassengerDTO passenger) {
        MissionDTO mission = findById(missionId);
        int missionWeightWithNewPassenger = missionRepository.totalPassengersWeight(passenger.getWeight(), missionId);


        if (bookingService.isPassengerAlreadyAffectedToGivenMission(passengerId, missionId)) {
            throw new MissionPassengerAlreadyAffectedToGivenMissionException(missionId);
        } else if (mission.getStatus() != MissionStatus.PLANNED) {
            throw new MissionStatusInvalidToAddPassengerException();
        } else if (!passenger.getMedicalClearance()) {
            throw new PassengerMedicalClearanceInvalidException();
        } else if (missionRepository.isMissionShipCapacityReached(missionId)) {
            throw new MissionShipCapacityExceedsException("Ship capacity is full, can't add more passenger",
                                                          HttpStatus.CONFLICT);
        } else if (missionWeightWithNewPassenger >= ship.getMaxWeight()) {
            throw new MissionShipWeightExceedsException();
        }
    }
}
