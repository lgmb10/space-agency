package com.lgambier.spaceagency.services;

import com.lgambier.spaceagency.dto.mission.MissionDTO;
import com.lgambier.spaceagency.dto.mission.request.MissionCreateRequestDTO;
import com.lgambier.spaceagency.dto.mission.request.MissionUpdateRequestDTO;
import com.lgambier.spaceagency.exceptions.mission.MissionNotFoundException;
import com.lgambier.spaceagency.exceptions.mission.MissionShipCapacityExceeds;
import com.lgambier.spaceagency.exceptions.mission.MissionShipTimeSlotAlreadyInUse;
import com.lgambier.spaceagency.models.Mission;
import com.lgambier.spaceagency.models.Ship;
import com.lgambier.spaceagency.repositories.MissionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MissionService {

    private final MissionRepository missionRepository;

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
    public void deleteById(Integer id) {
        findById(id);
        missionRepository.deleteById(id);
    }

    private void checkCapacity(Integer maxPassengers, Ship ship) {
        if (maxPassengers > ship.getCapacity()) {
            throw new MissionShipCapacityExceeds(maxPassengers, ship);
        }
    }

    private void checkDatesOverlap(Integer shipId, Mission mission, Boolean onUpdate) {
        if (missionRepository.existsOverlappingMission(shipId, mission.getDepartureDate(), mission.getArrivalDate())) {
            if(onUpdate) throw new MissionShipTimeSlotAlreadyInUse().onUpdate();
            else throw new MissionShipTimeSlotAlreadyInUse();
        }
    }


}
