package com.lgambier.spaceagency.controllers;


import com.lgambier.spaceagency.dto.booking.BookingDTO;
import com.lgambier.spaceagency.dto.mission.request.MissionAddPassengerDTO;
import com.lgambier.spaceagency.dto.passenger.PassengerDTO;
import com.lgambier.spaceagency.dto.ship.ShipDTO;
import com.lgambier.spaceagency.services.BookingService;
import com.lgambier.spaceagency.services.MissionService;
import com.lgambier.spaceagency.services.PassengerService;
import com.lgambier.spaceagency.services.ShipService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    private final MissionService missionService;

    private final ShipService shipService;

    private final PassengerService passengerService;

    @PostMapping("/{missionId}")
    public BookingDTO addPassengerToMission(@PathVariable("missionId") Integer missionId,
                                            @Valid @RequestBody MissionAddPassengerDTO passengerDTO) {
        ShipDTO shipDTO = shipService.findById(missionService
                                                       .findById(missionId)
                                                       .getShip()
                                                       .getId());
        PassengerDTO passenger = passengerService.findById(passengerDTO.getPassengerId());

        return BookingDTO.toDTO(bookingService.addPassenger(missionId, passengerDTO, shipDTO, passenger));
    }
}
