package com.lgambier.spaceagency.dto.passenger;

import com.lgambier.spaceagency.models.Passenger;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PassengerDTO {

    private Integer id;

    private String firstName;

    private String lastName;

    private String email;

    private Integer weight;

    private Boolean medicalClearance;

    public static PassengerDTO toDTO(Passenger passenger) {
        return PassengerDTO
                       .builder()
                       .id(passenger.getId())
                       .firstName(passenger.getFirstName())
                       .lastName(passenger.getLastName())
                       .email(passenger.getEmail())
                       .weight(passenger.getWeight())
                       .medicalClearance(passenger.getMedicalClearance())
                       .build();
    }
}
