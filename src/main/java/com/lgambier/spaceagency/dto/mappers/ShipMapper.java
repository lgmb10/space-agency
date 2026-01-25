package com.lgambier.spaceagency.dto.mappers;

import com.lgambier.spaceagency.dto.ship.ShipDTO;
import com.lgambier.spaceagency.models.Ship;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ShipMapper {
    ShipMapper INSTANCE = Mappers.getMapper(ShipMapper.class);

    ShipDTO shipToShipDto(Ship ship);

    Ship shipDtotoShip(ShipDTO shipDTO);
}
