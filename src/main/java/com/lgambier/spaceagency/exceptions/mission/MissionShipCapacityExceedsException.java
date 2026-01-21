package com.lgambier.spaceagency.exceptions.mission;

import com.lgambier.spaceagency.exceptions.config.GlobalException;
import com.lgambier.spaceagency.models.Ship;
import org.springframework.http.HttpStatus;

public class MissionShipCapacityExceedsException extends GlobalException {
    private static final String DEFAULT_MESSAGE = "Max passengers cannot exceed ship capacity";
    private static final HttpStatus HTTP_STATUS = HttpStatus.BAD_REQUEST;

    public MissionShipCapacityExceedsException(String cause, HttpStatus status) {
        super(cause, status);
    }

    public MissionShipCapacityExceedsException(Integer maxPassengers, Ship ship) {
        super(DEFAULT_MESSAGE+". "+maxPassengers +" (max passengers) > "+ship.getCapacity()+" (ship capacity)", HTTP_STATUS);
    }

    public MissionShipCapacityExceedsException() {
        super(DEFAULT_MESSAGE, HTTP_STATUS);
    }

}
