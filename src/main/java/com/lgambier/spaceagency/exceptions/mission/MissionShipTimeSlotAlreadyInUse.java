package com.lgambier.spaceagency.exceptions.mission;

import com.lgambier.spaceagency.exceptions.config.GlobalException;
import org.springframework.http.HttpStatus;

public class MissionShipTimeSlotAlreadyInUse extends GlobalException {
    private static final String DEFAULT_MESSAGE = "Ship time slot is already in another mission";
    private static final HttpStatus HTTP_STATUS = HttpStatus.CONFLICT;

    public MissionShipTimeSlotAlreadyInUse onUpdate() {
        return new MissionShipTimeSlotAlreadyInUse("Ship time slot is already in use by this mission or another mission");
    }

    public MissionShipTimeSlotAlreadyInUse(String message, HttpStatus status) {
        super(message, status);
    }

    public MissionShipTimeSlotAlreadyInUse(String message) {
        super(message, DEFAULT_STATUS);
    }

    public MissionShipTimeSlotAlreadyInUse() {
        super(DEFAULT_MESSAGE, HTTP_STATUS);
    }
}
