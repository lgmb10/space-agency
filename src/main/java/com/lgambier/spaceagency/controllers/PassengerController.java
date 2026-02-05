package com.lgambier.spaceagency.controllers;

import com.lgambier.spaceagency.dto.passenger.PassengerDTO;
import com.lgambier.spaceagency.dto.passenger.SanitizedPassengerDTO;
import com.lgambier.spaceagency.models.Passenger;
import com.lgambier.spaceagency.services.PassengerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Secured({"ROLE_ADMIN", "ROLE_PLANNER"})
@RestController
@RequestMapping("/passengers")
@RequiredArgsConstructor
@Tag(name = "Passengers", description = "Gestion des passagers")
public class PassengerController {

    private final PassengerService passengerService;

    @GetMapping
    @Operation(summary = "Récupérer la liste des passagers")
    public Page<PassengerDTO> getPassengersPaginated(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                     @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                                     @RequestParam(value = "sortBy", defaultValue = "lastName") String sortBy,
                                                     @RequestParam(value = "direction", defaultValue = "ASC") String direction) {

        Sort sort = direction.equalsIgnoreCase("DESC")
                            ? Sort.by(sortBy).descending()
                            : Sort.by(sortBy).ascending();
        return passengerService.getUsersPage(PageRequest.of(page, pageSize, sort));
    }

    @GetMapping("/mission/{missionId}")
    @Operation(summary = "Récupérer les informations  des passagers d'une mission")
    public List<SanitizedPassengerDTO> getMissionBookingPassengers(@PathVariable Integer missionId) {
        return passengerService.getMissionPassengers(missionId);
    }

    @GetMapping("/{passengerId}")
    @Operation(summary = "Récupérer les informations  d'un passager")
    public PassengerDTO getOnePassenger(@PathVariable Integer passengerId) {
        return passengerService.findById(passengerId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Créer un passager")
    public PassengerDTO createPassenger(@RequestBody Passenger passenger) {
        return passengerService.create(passenger);
    }

    @PutMapping
    @Operation(summary = "Mettre à jour les informations d'un passager")

    public PassengerDTO updatePassenger(@RequestBody Passenger passenger) {
        return passengerService.update(passenger);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un passager")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePassenger(@PathVariable("id") Integer id) {
        passengerService.deleteById(id);
    }


}
