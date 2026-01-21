package com.lgambier.spaceagency.repositories;

import com.lgambier.spaceagency.models.Mission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface MissionRepository extends JpaRepository<Mission, Integer> {

    @Query("""
                SELECT COUNT(m) > 0
                FROM Mission m
                WHERE m.ship.id = :shipId
                AND m.departureDate < :arrivalDate
                AND m.arrivalDate > :departureDate
                AND m.status != "CANCELLED"
            """)
    Boolean existsOverlappingMission(@Param("shipId") Integer shipId, @Param("departureDate") LocalDateTime departureDate, @Param("arrivalDate") LocalDateTime arrivalDate);

    @Query("""
                SELECT COUNT(m) > 0
                FROM Mission m
                WHERE m.ship.id = :shipId
                AND m.status IN ('PLANNED', 'IN_PROGRESS')
                AND m.arrivalDate >= now
            """)
    Boolean existPlannedOrInProgressMissionForShip(@Param("shipId") Integer shipId, @Param("now") LocalDateTime now);

    @Query("""
                SELECT SUM(p.weight) + :passengerWeight
                FROM Booking b
                JOIN Mission m ON m.id = b.missionId
                JOIN Passenger p ON p.id = b.passengerId
                WHERE m.id = :missionId
            """)
    Integer totalPassengersWeight(@Param("missionId") Integer missionId, @Param("passengerWeight") Integer passengerWeight);

    @Query("""
            SELECT COUNT(b) >= m.maxPassengers
            FROM Mission m
            LEFT JOIN Booking b ON m.id = b.missionId
            WHERE m.id = :missionId
    """)
    Boolean canAddPassengerToMissionShipForCapacity(@Param("missionId") Integer missionId);
}