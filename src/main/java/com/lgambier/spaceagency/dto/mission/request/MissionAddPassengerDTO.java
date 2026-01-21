package com.lgambier.spaceagency.dto.mission.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MissionAddPassengerDTO {

    @NotNull
    private Integer passengerId;
}
