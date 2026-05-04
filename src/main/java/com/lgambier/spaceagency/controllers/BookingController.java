package com.lgambier.spaceagency.controllers;


import com.lgambier.spaceagency.dto.booking.BookingDTO;
import com.lgambier.spaceagency.dto.mappers.BookingMapper;
import com.lgambier.spaceagency.dto.mappers.PassengerMapper;
import com.lgambier.spaceagency.dto.mappers.ShipMapper;
import com.lgambier.spaceagency.dto.mission.request.MissionAddPassengerDTO;
import com.lgambier.spaceagency.models.Passenger;
import com.lgambier.spaceagency.models.Ship;
import com.lgambier.spaceagency.security.SecurityUtils;
import com.lgambier.spaceagency.services.BookingService;
import com.lgambier.spaceagency.services.MissionService;
import com.lgambier.spaceagency.services.PassengerService;
import com.lgambier.spaceagency.services.ShipService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
@Tag(name = "Bookings", description = "Gestion des réservations")

public class BookingController {

    private final BookingService bookingService;

    private final MissionService missionService;

    private final ShipService shipService;

    private final PassengerService passengerService;

    @Secured({"ROLE_ADMIN", "ROLE_ASTRONAUT"})
    @PostMapping("/{missionId}")
    @Operation(summary = "Ajouter un passager à une mission")
    public BookingDTO addPassengerToMission(@PathVariable("missionId") Integer missionId,
                                            @Valid @RequestBody MissionAddPassengerDTO passengerDTO) {

        Ship ship = ShipMapper.INSTANCE.shipDtotoShip(shipService.findById(missionService
                                                                                   .findById(missionId)
                                                                                   .ship()
                                                                                   .getId()));
        Passenger passenger = PassengerMapper.INSTANCE.passengerDtoToPassenger(
                passengerService.findById(passengerDTO.passengerId()));

        return BookingMapper.INSTANCE.bookingToBookingDto(
                bookingService.addPassenger(missionId, passengerDTO, ship, passenger));
    }


    @GetMapping
    @Operation(summary = "Récupérer la liste des réservations d'un passager")
    public List<BookingDTO> getPassengerBookingsFromUser() {
        String email = SecurityUtils.getJwtUserEmail();

        Passenger passenger = PassengerMapper.INSTANCE.passengerDtoToPassenger(
                passengerService.findPassengerWithMatchingUserEmail(email));

        return bookingService.getPassengerBookings(passenger.getId());
    }
}
