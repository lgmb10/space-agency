package com.lgambier.spaceagency.services;


import com.lgambier.spaceagency.dto.ship.ShipDTO;
import com.lgambier.spaceagency.enums.ShipStatus;
import com.lgambier.spaceagency.exceptions.ship.ShipCannotDeleteMissionPlannedOrInProgressAssociated;
import com.lgambier.spaceagency.models.Ship;
import com.lgambier.spaceagency.repositories.MissionRepository;
import com.lgambier.spaceagency.repositories.ShipRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ShipServiceTest {


    @Mock
    ShipRepository shipRepository;

    @Mock
    MissionRepository missionRepository;

    @Mock
    TimeProvider timeProvider;

    ShipService shipService;

    MissionService missionService;

    @BeforeEach
    void setUp() {
        shipService = new ShipService(shipRepository, missionRepository, timeProvider);
        missionService = new MissionService(missionRepository);
    }

    @Test
    public void createShip() {
        final Ship ship = new Ship();
        ship.setCapacity(10);
        ship.setName("name-1");
        ship.setStatus(ShipStatus.RETIRED);
        ship.setCapacity(300);

        when(shipRepository.save(any(Ship.class))).thenReturn(ship);

        Ship savedShip = ShipDTO.toShip(shipService.create(ship));
        System.out.println("savedShip : " + savedShip);
        assertNotNull(savedShip, "The saved ship should not be null");

        verify(shipRepository).save(ship);
    }

    @Test
    public void deleteShip_shouldThrowException_whenMissionAffectedIsPlannedOrInProgress() {
        Ship ship = Ship
                            .builder()
                            .id(1)
                            .name("test-ship")
                            .status(ShipStatus.ACTIVE)
                            .maxWeight(700)
                            .capacity(10)
                            .build();

        LocalDateTime now = LocalDateTime.parse("2026-01-19T14:33:40");

        when(shipRepository.findById(ship.getId())).thenReturn(Optional.of(ship));

        when(timeProvider.now()).thenReturn(now);

        when(missionRepository.existPlannedOrInProgressMissionForShip(ship.getId(), now)).thenReturn(true);

        assertThrows(ShipCannotDeleteMissionPlannedOrInProgressAssociated.class,
                     () -> shipService.deleteById(ship.getId()));

        verify(shipRepository, never()).delete(any());
    }
}
