package com.lgambier.spaceagency.services;

import com.lgambier.spaceagency.exceptions.passenger.PassengerNotFoundException;
import com.lgambier.spaceagency.models.Passenger;
import com.lgambier.spaceagency.repositories.PassengerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PassengerService {

    private final PassengerRepository passengerRepository;


    public List<Passenger> findAll() {
        return passengerRepository.findAll();
    }

    public Passenger findById(Integer id) {
        return passengerRepository.findById(id).orElseThrow(() -> new PassengerNotFoundException(id));
    }


    @Transactional
    public Passenger create(Passenger passenger) {
        return passengerRepository.save(passenger);
    }

    @Transactional
    public Passenger update(Passenger passenger) {
        passengerRepository.findById(passenger.getId()).orElseThrow(() -> new PassengerNotFoundException(passenger.getId()));

        return passengerRepository.save(passenger);
    }

    @Transactional
    public void deleteById(Integer id) {
        this.findById(id);
        passengerRepository.deleteById(id);
    }

}
