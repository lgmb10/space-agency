package com.lgambier.spaceagency.dto.mission.request;


import com.lgambier.spaceagency.enums.MissionStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class MissionUpdateStatusRequestDTO {

    @NotNull
    Integer id;

    @NotNull
    private MissionStatus status;
}
