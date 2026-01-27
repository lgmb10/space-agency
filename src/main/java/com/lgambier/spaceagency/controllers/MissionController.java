package com.lgambier.spaceagency.controllers;

import com.lgambier.spaceagency.dto.booking.BookingDTO;
import com.lgambier.spaceagency.dto.mappers.PassengerMapper;
import com.lgambier.spaceagency.dto.mappers.ShipMapper;
import com.lgambier.spaceagency.dto.mission.MissionDTO;
import com.lgambier.spaceagency.dto.mission.request.MissionCreateRequestDTO;
import com.lgambier.spaceagency.dto.mission.request.MissionPatchRequestDTO;
import com.lgambier.spaceagency.dto.mission.request.MissionUpdateRequestDTO;
import com.lgambier.spaceagency.dto.mission.request.MissionUpdateStatusRequestDTO;
import com.lgambier.spaceagency.dto.ship.ShipDTO;
import com.lgambier.spaceagency.models.Passenger;
import com.lgambier.spaceagency.models.Ship;
import com.lgambier.spaceagency.security.SecurityUtils;
import com.lgambier.spaceagency.services.BookingService;
import com.lgambier.spaceagency.services.MissionService;
import com.lgambier.spaceagency.services.PassengerService;
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

    private final PassengerService passengerService;

    private final BookingService bookingService;


    @GetMapping
    public List<MissionDTO> getAllMissions() {
        return missionService.findAll();
    }

    @GetMapping("/{missionId}")
    public MissionDTO getOneMission(@PathVariable int missionId) {
        return missionService.findById(missionId);
    }

    @GetMapping("/me")
    public List<MissionDTO> getPassengerBookingsFromUser() {
        String email = SecurityUtils.getJwtUserEmail();

        Passenger passenger = PassengerMapper.INSTANCE.passengerDtoToPassenger(
                passengerService.findPassengerWithMatchingUserEmail(email));

        return bookingService.getPassengerMissions(passenger.getId());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MissionDTO createMission(@RequestBody MissionCreateRequestDTO mission) {
        Ship ship = ShipMapper.INSTANCE
                            .shipDtotoShip(shipService.findById(mission.getShipId()));
        return missionService.create(mission, ship);
    }

    @PutMapping
    public MissionDTO updateMission(@RequestBody MissionUpdateRequestDTO mission) {
        Ship ship = ShipMapper.INSTANCE
                            .shipDtotoShip(shipService.findById(mission.getShipId()));
        return missionService.update(mission, ship);
    }

    @PatchMapping
    public MissionDTO patchMission(@RequestBody MissionPatchRequestDTO mission) {
        Ship ship = ShipMapper.INSTANCE
                            .shipDtotoShip(shipService.findById(mission.getShipId()));
        return missionService.patch(mission, ship);
    }

    @PatchMapping("/status")
    public MissionDTO patchMissionStatus(@Valid @RequestBody MissionUpdateStatusRequestDTO mission) {
        return missionService.patchStatus(mission);
    }

    @DeleteMapping("/{missionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMission(@PathVariable("missionId") Integer missionId) {
        missionService.deleteById(missionId);
    }


}
