package com.lgambier.spaceagency.controllers;

import com.lgambier.spaceagency.models.Booking;
import com.lgambier.spaceagency.repositories.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingRepository bookingRepository;

    @PostMapping
    // DTO
    public Booking

}
