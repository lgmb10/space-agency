package com.lgambier.spaceagency.dto.ship;

import com.lgambier.spaceagency.enums.ShipStatus;
import com.lgambier.spaceagency.models.Ship;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShipDTO {

    private Integer id;

    private String name;

    private Integer capacity;

    private Integer maxWeight;

    private ShipStatus status;

    public static ShipDTO toDTO(Ship ship) {
        return ShipDTO
                       .builder()
                       .id(ship.getId())
                       .name(ship.getName())
                       .capacity(ship.getCapacity())
                       .maxWeight(ship.getMaxWeight())
                       .status(ship.getStatus())
                       .build();
    }

    public static Ship toShip(ShipDTO dto) {
        return Ship
                       .builder()
                       .id(dto.getId())
                       .name(dto.getName())
                       .capacity(dto.getCapacity())
                       .maxWeight(dto.getMaxWeight())
                       .status(dto.getStatus())
                       .build();
    }
}
