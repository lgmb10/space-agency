package com.lgambier.spaceagency.repositories;

import com.lgambier.spaceagency.models.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PassengerRepository extends JpaRepository<Passenger, Integer> {

    Optional<Passenger> findByEmail(String email);

    @Query("SELECT b.passenger FROM Booking b WHERE b.missionId = :missionId")
    List<Passenger> findPassengersByMissionId(@Param("missionId") Integer missionId);
}