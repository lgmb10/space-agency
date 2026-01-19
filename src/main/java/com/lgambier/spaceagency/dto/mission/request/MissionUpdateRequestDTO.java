package com.lgambier.spaceagency.dto.mission.request;

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
}