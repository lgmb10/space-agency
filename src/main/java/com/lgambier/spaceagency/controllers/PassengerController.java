package com.lgambier.spaceagency.controllers;

import com.lgambier.spaceagency.dto.passenger.PassengerDTO;
import com.lgambier.spaceagency.models.Passenger;
import com.lgambier.spaceagency.services.PassengerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class PassengerController {

    private final PassengerService passengerService;

    private final JsonMapper jsonMapper;

    @GetMapping("/passengers")
    public List<PassengerDTO> getAllPassengers() {
        return passengerService.findAll();
    }

    @GetMapping("/passengers/{passengerId}")
    public PassengerDTO getOnePassenger(@PathVariable Integer  passengerId) {
        return passengerService.findById(passengerId);
    }

    @PostMapping("/passengers")
    @ResponseStatus(HttpStatus.CREATED)
    public PassengerDTO createPassenger(@RequestBody Passenger passenger) {
        return passengerService.create(passenger);
    }

    @PutMapping("/passengers")
    public PassengerDTO updatePassenger(@RequestBody Passenger passenger) {
        return passengerService.update(passenger);
    }

//    @PatchMapping("/passengers/{passengerId}")
//    public Passenger patchEmployee(@PathVariable Integer passengerId, @RequestBody Map<String, Object> patchPayload){
//        Passenger passenger = passengerService.findById(passengerId);
//
//        if(passenger == null){
//            throw new RuntimeException("Employee id not found - "+passengerId);
//        }
//
//        if(patchPayload.containsKey("id")){
//            throw new RuntimeException("Employee id not allowed in request body - "+ passengerId);
//        }
//
//        Passenger patchedPassenger = jsonMapper.updateValue(passenger, patchPayload);
//
//        return passengerService.update(patchedPassenger);
//
//    } // PICK NEW CODE FROM US 3.2


    @DeleteMapping("/passengers/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePassenger(@PathVariable("id") Integer id){
        passengerService.deleteById(id);
    }


}
