package com.lgambier.spaceagency.repositories;

import com.lgambier.spaceagency.models.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {

    Optional<Booking> findByPassengerIdAndMissionId(Integer passengerId, Integer missionId);

}
