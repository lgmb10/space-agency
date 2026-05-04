package com.lgambier.spaceagency.dto.ship;

import com.lgambier.spaceagency.enums.ShipStatus;


public record ShipDTO(

        Integer id,

        String name,

        Integer capacity,

        Integer maxWeight,

        ShipStatus status) {
}
