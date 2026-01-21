package com.lgambier.spaceagency.exceptions.mission;

import com.lgambier.spaceagency.exceptions.config.GlobalException;
import org.springframework.http.HttpStatus;

public class MissionShipTimeSlotAlreadyInUseException extends GlobalException {
    private static final String DEFAULT_MESSAGE = "Ship time slot is already in use in another mission";
    private static final HttpStatus HTTP_STATUS = HttpStatus.CONFLICT;

    public MissionShipTimeSlotAlreadyInUseException onUpdate() {
        return new MissionShipTimeSlotAlreadyInUseException("Ship time slot is already in use by this mission or in another mission");
    }

    public MissionShipTimeSlotAlreadyInUseException(String message, HttpStatus status) {
        super(message, status);
    }

    public MissionShipTimeSlotAlreadyInUseException(String message) {
        super(message, DEFAULT_STATUS);
    }

    public MissionShipTimeSlotAlreadyInUseException() {
        super(DEFAULT_MESSAGE, HTTP_STATUS);
    }
}
