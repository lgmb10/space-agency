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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Missions", description = "Gestion des missions")
public class MissionController {

    private final MissionService missionService;

    private final ShipService shipService;

    private final PassengerService passengerService;


    @Secured({"ROLE_ADMIN", "ROLE_PLANNER", "ROLE_OPERATOR"})
    @GetMapping
    @Operation(summary = "Récupérer la liste des missions")
    public List<MissionDTO> getAllMissions() {
        return missionService.findAll();
    }

    @Secured({"ROLE_ADMIN", "ROLE_PLANNER", "ROLE_OPERATOR"})
    @GetMapping("/{missionId}")
    @Operation(summary = "Récupérer les informations  d'une mission")
    public MissionDTO getOneMission(@PathVariable int missionId) {
        return missionService.findById(missionId);
    }

    @Secured({"ROLE_ADMIN", "ROLE_PLANNER", "ROLE_ASTRONAUT", "ROLE_OPERATOR"})
    @GetMapping("/me")
    @Operation(summary = "Récupérer les missions d'un passager")
    public List<SanitizedMissionDTO> getPassengerBookingMissionsFromUser() {
        String email = SecurityUtils.getJwtUserEmail();

        Passenger passenger = PassengerMapper.INSTANCE.passengerDtoToPassenger(
                passengerService.findPassengerWithMatchingUserEmail(email));

        return missionService.findPassengerMissions(passenger.getId());
    }

    @Secured({"ROLE_ADMIN", "ROLE_PLANNER", "ROLE_ASTRONAUT", "ROLE_OPERATOR"})
    @GetMapping("/available")
    @Operation(summary = "Récupérer les missions disponibles")
    public List<SanitizedMissionDTO> getAvailableMissions() {
        return missionService.getAvailableMissions();
    }

    @Secured({"ROLE_ADMIN", "ROLE_PLANNER"})
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Créer une mission")
    public MissionDTO createMission(@RequestBody MissionCreateRequestDTO mission) {
        Ship ship = ShipMapper.INSTANCE.shipDtotoShip(shipService.findById(mission.getShipId()));
        return missionService.create(mission, ship);
    }

    @Secured({"ROLE_ADMIN", "ROLE_PLANNER"})
    @PutMapping
    @Operation(summary = "Mettre à jour les informations d'une mission")
    public MissionDTO updateMission(@RequestBody MissionUpdateRequestDTO mission) {
        Ship ship = ShipMapper.INSTANCE.shipDtotoShip(shipService.findById(mission.getShipId()));
        return missionService.update(mission, ship);
    }

    @Secured({"ROLE_ADMIN", "ROLE_PLANNER"})
    @PatchMapping
    @Operation(summary = "Mettre à jour les informations d'une mission")
    public MissionDTO patchMission(@RequestBody MissionPatchRequestDTO mission) {
        if(mission.getShipId() != null){
            Ship ship = ShipMapper.INSTANCE.shipDtotoShip(shipService.findById(mission.getShipId()));
            return missionService.patch(mission, ship);

        }

        return missionService.patch(mission, null);

    }

    @Secured({"ROLE_ADMIN", "ROLE_OPERATOR"})
    @PatchMapping("/status")
    @Operation(summary = "Mettre à jour le statut d'une mission")
    public MissionDTO patchMissionStatus(@Valid @RequestBody MissionUpdateStatusRequestDTO mission) {
        return missionService.patchStatus(mission);
    }

    @Secured({"ROLE_ADMIN", "ROLE_PLANNER"})
    @DeleteMapping("/{missionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Supprimer une mission")
    public void deleteMission(@PathVariable("missionId") Integer missionId) {
        missionService.deleteById(missionId);
    }


}
