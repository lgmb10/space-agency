package com.lgambier.spaceagency.dto.mission.request;

import com.lgambier.spaceagency.enums.MissionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class MissionCreateRequestDTO {

    private Integer shipId;

    private LocalDateTime departureDate;

    private LocalDateTime arrivalDate;

    private String origin;

    private String destination;

    private MissionStatus status;

    private Integer maxPassengers;
}