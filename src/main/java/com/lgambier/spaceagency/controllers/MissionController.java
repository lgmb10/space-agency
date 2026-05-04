package com.lgambier.spaceagency.controllers;

import com.lgambier.spaceagency.dto.mappers.PassengerMapper;
import com.lgambier.spaceagency.dto.mappers.ShipMapper;
import com.lgambier.spaceagency.dto.mission.MissionDTO;
import com.lgambier.spaceagency.dto.mission.SanitizedMissionDTO;
import com.lgambier.spaceagency.dto.mission.request.MissionCreateRequestDTO;
import com.lgambier.spaceagency.dto.mission.request.MissionPatchRequestDTO;
import com.lgambier.spaceagency.dto.mission.request.MissionUpdateRequestDTO;
import com.lgambier.spaceagency.dto.mission.request.MissionUpdateStatusRequestDTO;
import com.lgambier.spaceagency.models.Passenger;
import com.lgambier.spaceagency.models.Ship;
import com.lgambier.spaceagency.security.SecurityUtils;
import com.lgambier.spaceagency.services.MissionService;
import com.lgambier.spaceagency.services.PassengerService;
import com.lgambier.spaceagency.services.ShipService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Secured("ROLE_ADMIN")
@RestController
@RequestMapping("/missions")
@RequiredArgsConstructor
public class MissionController {

    private final MissionService missionService;

    private final ShipService shipService;

    private final PassengerService passengerService;


    @Secured({"ROLE_ADMIN", "ROLE_PLANNER", "ROLE_OPERATOR"})
    @GetMapping
    public List<MissionDTO> getAllMissions() {
        return missionService.findAll();
    }

    @Secured({"ROLE_ADMIN", "ROLE_PLANNER", "ROLE_OPERATOR"})
    @GetMapping("/{missionId}")
    public MissionDTO getOneMission(@PathVariable int missionId) {
        return missionService.findById(missionId);
    }

    @Secured({"ROLE_ADMIN", "ROLE_PLANNER", "ROLE_ASTRONAUT", "ROLE_OPERATOR"})
    @GetMapping("/me")
    public List<SanitizedMissionDTO> getPassengerBookingMissionsFromUser() {
        String email = SecurityUtils.getJwtUserEmail();

        Passenger passenger = PassengerMapper.INSTANCE.passengerDtoToPassenger(
                passengerService.findPassengerWithMatchingUserEmail(email));

        return missionService.findPassengerMissions(passenger.getId());
    }

    @Secured({"ROLE_ADMIN", "ROLE_PLANNER", "ROLE_ASTRONAUT", "ROLE_OPERATOR"})
    @GetMapping("/available")
    public List<SanitizedMissionDTO> getAvailableMissions() {
        return missionService.getAvailableMissions();
    }

    @Secured({"ROLE_ADMIN", "ROLE_PLANNER"})
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MissionDTO createMission(@RequestBody MissionCreateRequestDTO mission) {
        Ship ship = ShipMapper.INSTANCE.shipDtotoShip(shipService.findById(mission.getShipId()));
        return missionService.create(mission, ship);
    }

    @Secured({"ROLE_ADMIN", "ROLE_PLANNER"})
    @PutMapping
    public MissionDTO updateMission(@RequestBody MissionUpdateRequestDTO mission) {
        Ship ship = ShipMapper.INSTANCE.shipDtotoShip(shipService.findById(mission.getShipId()));
        return missionService.update(mission, ship);
    }

    @Secured({"ROLE_ADMIN", "ROLE_PLANNER"})
    @PatchMapping
    public MissionDTO patchMission(@RequestBody MissionPatchRequestDTO mission) {
        if(mission.getShipId() != null){
            Ship ship = ShipMapper.INSTANCE.shipDtotoShip(shipService.findById(mission.getShipId()));
            return missionService.patch(mission, ship);

        }

        return missionService.patch(mission, null);

    }

    @Secured({"ROLE_ADMIN", "ROLE_OPERATOR"})
    @PatchMapping("/status")
    public MissionDTO patchMissionStatus(@Valid @RequestBody MissionUpdateStatusRequestDTO mission) {
        return missionService.patchStatus(mission);
    }

    @Secured({"ROLE_ADMIN", "ROLE_PLANNER"})
    @DeleteMapping("/{missionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMission(@PathVariable("missionId") Integer missionId) {
        missionService.deleteById(missionId);
    }


}
