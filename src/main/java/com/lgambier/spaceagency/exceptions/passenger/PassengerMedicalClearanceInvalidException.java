package com.lgambier.spaceagency.exceptions.passenger;

import com.lgambier.spaceagency.exceptions.config.GlobalException;
import org.springframework.http.HttpStatus;

public class PassengerMedicalClearanceInvalidException extends GlobalException {
    private static final String DEFAULT_MESSAGE = "The passenger medical clearance is invalid";
    private static final HttpStatus HTTP_STATUS = HttpStatus.BAD_REQUEST;


    public PassengerMedicalClearanceInvalidException(String cause) {
        super(cause, HTTP_STATUS);
    }

    public PassengerMedicalClearanceInvalidException() {
        super(DEFAULT_MESSAGE, HTTP_STATUS);
    }



}
