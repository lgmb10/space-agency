package com.lgambier.spaceagency.services;

import com.lgambier.spaceagency.models.Booking;
import com.lgambier.spaceagency.repositories.BookingRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;

    @Transactional
    public Booking addPassenger(Integer missionId, Integer passengerId){
        Booking booking = Booking.builder().passengerId(passengerId).missionId(missionId).build();

        return bookingRepository.save(booking);
    }

    public boolean isPassengerAlreadyAffectedToGivenMission(Integer missionId, Integer passengerId){
        return bookingRepository.findByPassengerIdAndMissionId(missionId, passengerId).isPresent();
    }
}
