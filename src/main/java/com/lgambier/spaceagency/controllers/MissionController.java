package com.lgambier.spaceagency.controllers;

import com.lgambier.spaceagency.dto.mission.request.MissionCreateRequestDTO;
import com.lgambier.spaceagency.dto.mission.MissionDTO;
import com.lgambier.spaceagency.dto.mission.request.MissionUpdateRequestDTO;
import com.lgambier.spaceagency.dto.ship.ShipDTO;
import com.lgambier.spaceagency.models.Mission;
import com.lgambier.spaceagency.models.Ship;
import com.lgambier.spaceagency.services.MissionService;
import com.lgambier.spaceagency.services.ShipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MissionController {

    private final MissionService missionService;

    private final ShipService shipService;

    private final JsonMapper jsonMapper;

    @GetMapping("/missions")
    public List<MissionDTO> getAllMissions() {
        return missionService.findAll();
    }

    @GetMapping("/missions/{missionId}")
    public MissionDTO getOneMission(@PathVariable int  missionId) {
        return missionService.findById(missionId);
    }

    @PostMapping("/missions")
    @ResponseStatus(HttpStatus.CREATED)
    public MissionDTO createMission(@RequestBody MissionCreateRequestDTO mission) {
        Ship ship = ShipDTO.toShip(shipService.findById(mission.getShipId()));
        return missionService.create(mission, ship);
    }

    @PutMapping("/missions")
    public MissionDTO updateMission(@RequestBody MissionUpdateRequestDTO mission) {
        Ship ship = ShipDTO.toShip(shipService.findById(mission.getShipId()));
        return missionService.update(mission, ship);
    }

    @DeleteMapping("/missions/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMission(@PathVariable("id") Integer id){
        missionService.deleteById(id);
    }


}
