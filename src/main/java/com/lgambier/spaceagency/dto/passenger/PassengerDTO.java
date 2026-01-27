package com.lgambier.spaceagency.dto.passenger;

public record PassengerDTO(

        Integer id,

        String firstName,

        String lastName,

        String email,

        Integer weight,

        Boolean medicalClearance) {
}
