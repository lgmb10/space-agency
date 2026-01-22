package com.lgambier.spaceagency.dto.mission.request;

import com.lgambier.spaceagency.enums.MissionStatus;
import com.lgambier.spaceagency.models.Mission;
import com.lgambier.spaceagency.models.Ship;
import lombok.AllArgsConstructor;
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
    
    public static Mission toMission(MissionCreateRequestDTO dto, Ship ship){
        return Mission
                       .builder()
                       .ship(ship)
                       .departureDate(dto.getDepartureDate())
                       .arrivalDate(dto.getArrivalDate())
                       .origin(dto.getOrigin())
                       .destination(dto.getDestination())
                       .status(dto.getStatus())
                       .maxPassengers(dto.getMaxPassengers())
                       .build();
    }
}