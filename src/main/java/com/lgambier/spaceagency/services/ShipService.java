package com.lgambier.spaceagency.services;

import com.lgambier.spaceagency.dto.mappers.ShipMapper;
import com.lgambier.spaceagency.dto.ship.ShipDTO;
import com.lgambier.spaceagency.exceptions.ship.ShipCannotDeleteMissionPlannedOrInProgressAssociatedException;
import com.lgambier.spaceagency.exceptions.ship.ShipNotFoundException;
import com.lgambier.spaceagency.models.Ship;
import com.lgambier.spaceagency.repositories.MissionRepository;
import com.lgambier.spaceagency.repositories.ShipRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShipService {

    private final ShipRepository shipRepository;

    private final MissionRepository missionRepository;

    private final TimeProvider timeProvider;

    public List<ShipDTO> findAll() {
        List<Ship> ships = shipRepository.findAll();
        return ships
                       .stream()
                       .map(ShipMapper.INSTANCE::shipToShipDto)
                       .collect(Collectors.toList());
    }


    public ShipDTO findById(Integer id) {
        Ship ship = shipRepository
                            .findById(id)
                            .orElseThrow(() -> new ShipNotFoundException(id));

        return ShipMapper.INSTANCE.shipToShipDto(ship);
    }


    @Transactional
    public ShipDTO create(Ship ship) {
        return ShipMapper.INSTANCE.shipToShipDto(shipRepository.save(ship));
    }


    @Transactional
    public ShipDTO update(Ship ship) {
        shipRepository
                .findById(ship.getId())
                .orElseThrow(() -> new ShipNotFoundException(ship.getId()));

        return ShipMapper.INSTANCE.shipToShipDto(shipRepository.save(ship));
    }


    @Transactional
    public void deleteById(Integer id) {
        findById(id);
        isShipAssociatedToPlannedOrInProgressMission(id, timeProvider);
        shipRepository.deleteById(id);
    }

    private void isShipAssociatedToPlannedOrInProgressMission(Integer shipId, TimeProvider timeProvider) {
        if (missionRepository.existPlannedOrInProgressMissionForShip(shipId, timeProvider.now())) {
            throw new ShipCannotDeleteMissionPlannedOrInProgressAssociatedException();
        }
    }

}
