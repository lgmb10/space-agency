package com.lgambier.spaceagency.dto.mission;

import com.lgambier.spaceagency.enums.MissionStatus;
import com.lgambier.spaceagency.models.Ship;

import java.time.LocalDateTime;

public record MissionDTO(

        Integer id,

        Ship ship,

        LocalDateTime departureDate,

        LocalDateTime arrivalDate,

        String origin,

        String destination,

        MissionStatus status,

        Integer maxPassengers) {
}
