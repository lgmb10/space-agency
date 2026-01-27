package com.lgambier.spaceagency.dto.mission;

import com.lgambier.spaceagency.enums.MissionStatus;

import java.time.LocalDateTime;

public record SanitizedMissionDTO(

        Integer id,

        String shipName,

        LocalDateTime departureDate,

        LocalDateTime arrivalDate,

        String origin,

        String destination,

        MissionStatus status) {
}
