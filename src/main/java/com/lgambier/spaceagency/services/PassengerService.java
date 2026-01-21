package com.lgambier.spaceagency.services;

import com.lgambier.spaceagency.dto.passenger.PassengerDTO;
import com.lgambier.spaceagency.exceptions.passenger.PassengerNotFoundException;
import com.lgambier.spaceagency.models.Passenger;
import com.lgambier.spaceagency.repositories.PassengerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PassengerService {

    private final PassengerRepository passengerRepository;

    public List<PassengerDTO> findAll() {
        List<Passenger> passengers = passengerRepository.findAll();
        return passengers
                       .stream()
                       .map(PassengerDTO::toDTO)
                       .collect(Collectors.toList());
    }

    public PassengerDTO findById(Integer id) {
       Passenger passenger = passengerRepository
                       .findById(id)
                       .orElseThrow(() -> new PassengerNotFoundException(id));

       return PassengerDTO.toDTO(passenger);
    }

    @Transactional
    public PassengerDTO create(Passenger passenger) {
        return PassengerDTO.toDTO(passengerRepository.save(passenger));
    }

    @Transactional
    public PassengerDTO update(Passenger passenger) {
        passengerRepository
                .findById(passenger.getId())
                .orElseThrow(() -> new PassengerNotFoundException(passenger.getId()));

        return PassengerDTO.toDTO(passengerRepository.save(passenger));
    }

    @Transactional
    public void deleteById(Integer id) {
        this.findById(id);
        passengerRepository.deleteById(id);
    }

}
