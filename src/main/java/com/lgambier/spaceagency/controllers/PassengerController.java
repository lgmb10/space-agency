package com.lgambier.spaceagency.controllers;

import com.lgambier.spaceagency.dto.passenger.PassengerDTO;
import com.lgambier.spaceagency.dto.passenger.SanitizedPassengerDTO;
import com.lgambier.spaceagency.models.Passenger;
import com.lgambier.spaceagency.services.PassengerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Secured({"ROLE_ADMIN", "ROLE_PLANNER"})
@RestController
@RequestMapping("/passengers")
@RequiredArgsConstructor
public class PassengerController {

    private final PassengerService passengerService;


    @GetMapping
    public List<PassengerDTO> getAllPassengers() {
        return passengerService.findAll();
    }

    @GetMapping("/mission/{missionId}")
    public List<SanitizedPassengerDTO> getMissionBookingPassengers(@PathVariable Integer missionId) {
        return passengerService.getMissionPassengers(missionId);
    }

    @GetMapping("/{passengerId}")
    public PassengerDTO getOnePassenger(@PathVariable Integer passengerId) {
        return passengerService.findById(passengerId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PassengerDTO createPassenger(@RequestBody Passenger passenger) {
        return passengerService.create(passenger);
    }

    @PutMapping
    public PassengerDTO updatePassenger(@RequestBody Passenger passenger) {
        return passengerService.update(passenger);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePassenger(@PathVariable("id") Integer id) {
        passengerService.deleteById(id);
    }


}
