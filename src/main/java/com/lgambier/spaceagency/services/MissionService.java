package com.lgambier.spaceagency.services;

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

@Service
@RequiredArgsConstructor
public class MissionService {

    private final MissionRepository missionRepository;

    private final ShipService shipService;


    public List<Mission> findAll() {
        return missionRepository.findAll();
    }

    public Mission findById(int id) {
        return missionRepository.findById(id).orElseThrow(() -> new MissionNotFoundException(id));
    }


    @Transactional
    public Mission create(MissionCreateRequestDTO missionRequest) {
        Ship ship = shipService.findById(missionRequest.getShipId());
        checkCapacity(missionRequest.getMaxPassengers(), ship);

        Mission mission = Mission.builder().ship(ship).departureDate(missionRequest.getDepartureDate()).arrivalDate(missionRequest.getArrivalDate()).origin(missionRequest.getOrigin()).destination(missionRequest.getDestination()).status(missionRequest.getStatus()).maxPassengers(missionRequest.getMaxPassengers()).build();
        checkDatesOverlap(ship.getId(), mission);


        return missionRepository.save(mission);
    }

    @Transactional
    public Mission update(MissionUpdateRequestDTO missionRequest) {
        findById(missionRequest.getId());

        Ship ship = shipService.findById(missionRequest.getShipId());
        checkCapacity(missionRequest.getMaxPassengers(), ship);

        Mission mission = Mission.builder().ship(ship).departureDate(missionRequest.getDepartureDate()).arrivalDate(missionRequest.getArrivalDate()).origin(missionRequest.getOrigin()).destination(missionRequest.getDestination()).status(missionRequest.getStatus()).maxPassengers(missionRequest.getMaxPassengers()).build();
        checkDatesOverlap(ship.getId(), mission);


        return missionRepository.save(mission);
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

    private void checkDatesOverlap(int shipId, Mission mission){
        if(missionRepository.existsOverlappingMission(shipId, mission.getDepartureDate(), mission.getArrivalDate())){
            throw new MissionShipTimeSlotAlreadyInUse();
        }
    }


}
