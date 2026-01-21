package com.lgambier.spaceagency.dto.mission.request;

import com.lgambier.spaceagency.models.Mission;
import com.lgambier.spaceagency.models.Ship;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class MissionUpdateRequestDTO extends MissionCreateRequestDTO {
    private Integer id;

    public static Mission toMission(MissionUpdateRequestDTO dto, Ship ship) {
        return Mission
                       .builder()
                       .id(dto.getId())
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