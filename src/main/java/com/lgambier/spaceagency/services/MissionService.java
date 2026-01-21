package com.lgambier.spaceagency.services;

import com.lgambier.spaceagency.dto.mission.MissionDTO;
import com.lgambier.spaceagency.dto.mission.request.MissionCreateRequestDTO;
import com.lgambier.spaceagency.dto.mission.request.MissionPatchRequestDTO;
import com.lgambier.spaceagency.dto.mission.request.MissionUpdateRequestDTO;
import com.lgambier.spaceagency.dto.mission.request.MissionUpdateStatusRequestDTO;
import com.lgambier.spaceagency.enums.MissionStatus;
import com.lgambier.spaceagency.exceptions.mission.MissionNotFoundException;
import com.lgambier.spaceagency.exceptions.mission.MissionShipCapacityExceedsException;
import com.lgambier.spaceagency.exceptions.mission.MissionShipTimeSlotAlreadyInUseException;
import com.lgambier.spaceagency.exceptions.mission.MissionTransitionException;
import com.lgambier.spaceagency.models.Mission;
import com.lgambier.spaceagency.models.Ship;
import com.lgambier.spaceagency.repositories.MissionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tools.jackson.databind.json.JsonMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MissionService {

    private final MissionRepository missionRepository;

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
    public Mission patch(MissionPatchRequestDTO missionRequest) {
        Mission mission = findById(missionRequest.getId());
        Mission patchedMission = createMissionFromPatchDTO(missionRequest, mission);

        return missionRepository.save(patchedMission);
    }

    @Transactional
    public Mission patchStatus(MissionUpdateStatusRequestDTO missionRequest) {
        Mission mission = findById(missionRequest.getId());
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
                else if(mission.getDepartureDate().isAfter(LocalDateTime.now())){
                    throw new MissionTransitionException(oldStatus, newStatus, "You cannot set this mission status to PLANNED because the departure date is in the future.");
                }
            }
        }

        if(hasToThrow) throw new MissionTransitionException(oldStatus, newStatus);

        mission.setStatus(missionRequest.getStatus());
        return missionRepository.save(mission);
    }

    @Transactional
    public void deleteById(Integer id) {
        findById(id);
        missionRepository.deleteById(id);
    }

    private void checkCapacity(Integer maxPassengers, Ship ship) {
        if (maxPassengers > ship.getCapacity()) {
            throw new MissionShipCapacityExceedsException(maxPassengers, ship);
        }
    }

    private void checkDatesOverlap(Integer shipId, Mission mission, Boolean onUpdate) {
        if (missionRepository.existsOverlappingMission(shipId, mission.getDepartureDate(), mission.getArrivalDate())) {
            if(onUpdate) throw new MissionShipTimeSlotAlreadyInUseException().onUpdate();
            else throw new MissionShipTimeSlotAlreadyInUseException();
        }
    }

    private Mission createMissionFromPatchDTO(MissionPatchRequestDTO missionRequest, Mission currentMission) {
        Mission patchedMission = jsonMapper.updateValue(currentMission, missionRequest);

        if (missionRequest.getShipId() != null) {
            Ship ship = shipService.findById(missionRequest.getShipId());
            checkCapacity(missionRequest.getMaxPassengers(), ship);
            patchedMission.setShip(ship);
            checkDatesOverlap(ship.getId(), currentMission);
        }

        return patchedMission;
    }


}
