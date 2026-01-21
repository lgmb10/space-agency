package com.lgambier.spaceagency.dto.mission;

import com.lgambier.spaceagency.enums.MissionStatus;
import com.lgambier.spaceagency.models.Mission;
import com.lgambier.spaceagency.models.Ship;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MissionDTO {

    private Integer id;

    private Ship ship;

    private LocalDateTime departureDate;

    private LocalDateTime arrivalDate;

    private String origin;

    private String destination;

    private MissionStatus status;

    private Integer maxPassengers;

    public static MissionDTO toDTO(Mission mission) {
        return MissionDTO
                       .builder()
                       .id(mission.getId())
                       .ship(mission.getShip())
                       .departureDate(mission.getDepartureDate())
                       .arrivalDate(mission.getArrivalDate())
                       .origin(mission.getOrigin())
                       .destination(mission.getDestination())
                       .status(mission.getStatus())
                       .maxPassengers(mission.getMaxPassengers())
                       .build();
    }

}
