package com.lgambier.spaceagency.services;

import com.lgambier.spaceagency.dto.passenger.PassengerDTO;
import com.lgambier.spaceagency.models.Passenger;
import com.lgambier.spaceagency.repositories.PassengerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PassengerServiceTest {

    @Mock
    PassengerRepository passengerRepository;

    @InjectMocks
    PassengerService passengerService;


    @Test
    public void createPassenger() {
        final Passenger passenger = Passenger
                                            .builder()
                                            .email("test@gmail.com")
                                            .firstName("toto")
                                            .lastName("tata")
                                            .weight(2)
                                            .build();

        when(passengerRepository.save(any(Passenger.class))).thenReturn(passenger);

        PassengerDTO savedPassenger = passengerService.create(passenger);
        System.out.println("saved passenger : " + savedPassenger);
        assertNotNull(savedPassenger, "The saved passenger should not be null");

        verify(passengerRepository).save(passenger);


    }


    // MOVE TO INTEGRATION TEST
//
//    @Test
//    public void createPassengers_shouldThrowException_whenEmailAlreadyExist(){
//
//
//    }
//    @Test
//    public void createPassenger_shouldThrowException_whenEmailFormatIsInvalid(){
//        final Passenger passenger = Passenger.builder().email("wrongemail.com").firstName("toto").lastName("tata").weight(2).build();
//
//        when(passengerRepository.save(any())).thenThrow(new ConstraintViolationException("Invalid data", Set.of()));
//        assertThrows(ConstraintViolationException.class, () -> passengerService.create(passenger));
//
//        verify(passengerRepository).save(passenger);
//    }
//
//    @Test
//    public void createPassenger_shouldThrowException_whenWeightIsNegative(){
//        final Passenger passenger = Passenger.builder().email("email@gmail.com").firstName("toto").lastName("tata").weight(-2).build();
//
//        assertThrows(ConstraintViolationException.class, () -> passengerService.create(passenger));
//
//        verify(passengerRepository).save(passenger);
//    }
}
