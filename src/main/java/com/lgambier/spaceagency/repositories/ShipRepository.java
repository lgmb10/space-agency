package com.lgambier.spaceagency.repositories;

import com.lgambier.spaceagency.models.Ship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShipRepository extends JpaRepository<Ship, Integer> {
}