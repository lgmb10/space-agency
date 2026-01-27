package com.lgambier.spaceagency.repositories;

import com.lgambier.spaceagency.models.Passenger;
import com.lgambier.spaceagency.models.Ship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PassengerRepository extends JpaRepository<Passenger, Integer> {

    Optional<Passenger> findByEmail(String email);
}