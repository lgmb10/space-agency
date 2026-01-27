package com.lgambier.spaceagency.dto.mappers;

import com.lgambier.spaceagency.dto.mission.MissionDTO;
import com.lgambier.spaceagency.dto.mission.SanitizedMissionDTO;
import com.lgambier.spaceagency.models.Mission;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MissionMapper {
    MissionMapper INSTANCE = Mappers.getMapper(MissionMapper.class);

    MissionDTO missionToMissionDto(Mission mission);

    Mission missionDtoToMission(MissionDTO missionDTO);

    @Mapping(source = "ship.name", target = "shipName")
    SanitizedMissionDTO missionToSanitizedMissionDto(Mission mission);
}
