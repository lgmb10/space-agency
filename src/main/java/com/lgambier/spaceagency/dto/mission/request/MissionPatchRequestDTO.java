package com.lgambier.spaceagency.dto.mission.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MissionPatchRequestDTO extends MissionUpdateRequestDTO {
}