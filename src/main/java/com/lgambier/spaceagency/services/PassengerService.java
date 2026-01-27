package com.lgambier.spaceagency.services;

import com.lgambier.spaceagency.dto.mappers.PassengerMapper;
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
                       .map(PassengerMapper.INSTANCE::passengerToPassengerDto)
                       .collect(Collectors.toList());
    }

    public PassengerDTO findById(Integer id) {
        Passenger passenger = passengerRepository
                                      .findById(id)
                                      .orElseThrow(() -> new PassengerNotFoundException(id));

        return PassengerMapper.INSTANCE.passengerToPassengerDto(passenger);
    }

    public PassengerDTO findPassengerWithMatchingUserEmail(String email) {
        Passenger passenger = passengerRepository
                                      .findByEmail(email)
                                      .orElseThrow(PassengerNotFoundException::new);

        return PassengerMapper.INSTANCE.passengerToPassengerDto(passenger);
    }

    @Transactional
    public PassengerDTO create(Passenger passenger) {
        return PassengerMapper.INSTANCE.passengerToPassengerDto(passengerRepository.save(passenger));
    }

    @Transactional
    public PassengerDTO update(Passenger passenger) {
        passengerRepository
                .findById(passenger.getId())
                .orElseThrow(() -> new PassengerNotFoundException(passenger.getId()));

        return PassengerMapper.INSTANCE.passengerToPassengerDto(passengerRepository.save(passenger));
    }

    @Transactional
    public void deleteById(Integer id) {
        this.findById(id);
        passengerRepository.deleteById(id);
    }

}
