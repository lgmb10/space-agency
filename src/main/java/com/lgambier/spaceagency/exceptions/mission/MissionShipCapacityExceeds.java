package com.lgambier.spaceagency.exceptions.mission;

import com.lgambier.spaceagency.exceptions.config.GlobalException;
import com.lgambier.spaceagency.models.Ship;
import org.springframework.http.HttpStatus;

public class MissionShipCapacityExceeds extends GlobalException {
    private static final String DEFAULT_MESSAGE = "maxPassengers cannot exceed ship capacity";
    private static final HttpStatus HTTP_STATUS = HttpStatus.BAD_REQUEST;

    public MissionShipCapacityExceeds(Integer maxPassengers, Ship ship) {
        super(DEFAULT_MESSAGE+". "+maxPassengers +" (max passengers) > "+ship.getCapacity()+" (ship capacity)", HTTP_STATUS);
    }

    public MissionShipCapacityExceeds() {
        super(DEFAULT_MESSAGE, HTTP_STATUS);
    }

}
