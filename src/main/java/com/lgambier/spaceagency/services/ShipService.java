package com.lgambier.spaceagency.services;

import com.lgambier.spaceagency.exceptions.ship.ShipCannotDeleteMissionPlannedOrInProgressAssociated;
import com.lgambier.spaceagency.exceptions.ship.ShipNotFoundException;
import com.lgambier.spaceagency.models.Ship;
import com.lgambier.spaceagency.repositories.MissionRepository;
import com.lgambier.spaceagency.repositories.ShipRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShipService {

    private final ShipRepository shipRepository;

    private final MissionRepository missionRepository;

    private final TimeProvider timeProvider;

    public List<Ship> findAll() {
        return shipRepository.findAll();
    }

    public Ship findById(Integer id) {
        return shipRepository.findById(id).orElseThrow(() -> new ShipNotFoundException(id));
    }


    @Transactional
    public Ship create(Ship ship) {
        return shipRepository.save(ship);
    }

    @Transactional
    public Ship update(Ship ship) {
        shipRepository.findById(ship.getId()).orElseThrow(() -> new ShipNotFoundException(ship.getId()));

        return shipRepository.save(ship);
    }

    @Transactional
    public void deleteById(Integer id) {
        this.findById(id);
        this.isShipAssociatedToPlannedOrInProgressMission(id, timeProvider);

        shipRepository.deleteById(id);
    }

    private void isShipAssociatedToPlannedOrInProgressMission(Integer shipId, TimeProvider timeProvider){
        if(missionRepository.existPlannedOrInProgressMissionForShip(shipId, timeProvider.now())){
            throw new ShipCannotDeleteMissionPlannedOrInProgressAssociated();
        }
    }

}
