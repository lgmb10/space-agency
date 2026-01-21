package com.lgambier.spaceagency.controllers;

import com.lgambier.spaceagency.dto.mission.request.MissionCreateRequestDTO;
import com.lgambier.spaceagency.dto.mission.MissionDTO;
import com.lgambier.spaceagency.dto.mission.request.MissionPatchRequestDTO;
import com.lgambier.spaceagency.dto.mission.request.MissionUpdateRequestDTO;
import com.lgambier.spaceagency.dto.ship.ShipDTO;
import com.lgambier.spaceagency.dto.mission.request.MissionUpdateStatusRequestDTO;
import com.lgambier.spaceagency.models.Ship;
import com.lgambier.spaceagency.dto.mission.request.*;
import com.lgambier.spaceagency.models.Booking;
import com.lgambier.spaceagency.services.MissionService;
import com.lgambier.spaceagency.services.ShipService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/missions")
@RequiredArgsConstructor
public class MissionController {

    private final MissionService missionService;

    private final ShipService shipService;

    @GetMapping
    public List<MissionDTO> getAllMissions() {
        return missionService.findAll();
    }

    @GetMapping("/{missionId}")
    public MissionDTO getOneMission(@PathVariable int  missionId) {
        return missionService.findById(missionId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MissionDTO createMission(@RequestBody MissionCreateRequestDTO mission) {
        Ship ship = ShipDTO.toShip(shipService.findById(mission.getShipId()));
        return missionService.create(mission, ship);
    }

    @PostMapping("/{missionId}/passengers")
    public Booking addPassengerToMission(@PathVariable("missionId") Integer missionId, @Valid @RequestBody MissionAddPassengerDTO passengerDTO) {
        return missionService.addPassenger(missionId, passengerDTO);
    }

    @PutMapping
    public MissionDTO updateMission(@RequestBody MissionUpdateRequestDTO mission) {
        Ship ship = ShipDTO.toShip(shipService.findById(mission.getShipId()));
        return missionService.update(mission, ship);
    }

    @PatchMapping
    public MissionDTO patchMission(@RequestBody MissionPatchRequestDTO mission){
        Ship ship = ShipDTO.toShip(shipService.findById(mission.getShipId()));
        return missionService.patch(mission, ship);
    }

    @PatchMapping("/status")
    public MissionDTO patchMissionStatus(@Valid @RequestBody MissionUpdateStatusRequestDTO mission){
        return missionService.patchStatus(mission);
    }

    @DeleteMapping("/{missionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMission(@PathVariable("missionId") Integer missionId) {
        missionService.deleteById(missionId);
    }


}
