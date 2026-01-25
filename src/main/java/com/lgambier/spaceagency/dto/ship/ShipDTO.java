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
}
