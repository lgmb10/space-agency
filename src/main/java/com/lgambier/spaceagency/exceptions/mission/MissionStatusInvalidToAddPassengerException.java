package com.lgambier.spaceagency.exceptions.mission;

import com.lgambier.spaceagency.exceptions.config.GlobalException;
import org.springframework.http.HttpStatus;

public class MissionStatusInvalidToAddPassengerException extends GlobalException {
    private static final String DEFAULT_MESSAGE = "You can’t add a passenger to a mission that does not have the status PLANNED";
    private static final HttpStatus HTTP_STATUS = HttpStatus.BAD_REQUEST;

    public MissionStatusInvalidToAddPassengerException(String message, HttpStatus status) {
        super(message, status);
    }

    public MissionStatusInvalidToAddPassengerException(String message) {
        super(message, DEFAULT_STATUS);
    }

    public MissionStatusInvalidToAddPassengerException() {
        super(DEFAULT_MESSAGE, HTTP_STATUS);
    }
}
