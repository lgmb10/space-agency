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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        Ship ship = ShipMapper.INSTANCE.shipDtotoShip(shipService.findById(missionService
                                                                                   .findById(missionId)
                                                                                   .getShip()
                                                                                   .getId()));
        Passenger passenger = PassengerMapper.INSTANCE.passengerDtoToPassenger(
                passengerService.findById(passengerDTO.getPassengerId()));

        return BookingMapper.INSTANCE.bookingToBookingDto(
                bookingService.addPassenger(missionId, passengerDTO, ship, passenger));
    }


    @GetMapping
    public List<BookingDTO> getPassengerBookingsFromUser() {
        String email = SecurityUtils.getJwtUserEmail();

        Passenger passenger = PassengerMapper.INSTANCE.passengerDtoToPassenger(
                passengerService.findPassengerWithMatchingUserEmail(email));

        return bookingService.getPassengerBookings(passenger.getId());
    }
}
