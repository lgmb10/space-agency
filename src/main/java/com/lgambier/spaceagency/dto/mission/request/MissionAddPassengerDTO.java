package com.lgambier.spaceagency.dto.mission.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class MissionAddPassengerDTO {

    @NotNull
    private Integer passengerId;
}
