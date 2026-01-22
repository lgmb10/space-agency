package com.lgambier.spaceagency.exceptions.passenger;

import com.lgambier.spaceagency.exceptions.config.GlobalException;
import org.springframework.http.HttpStatus;


public class PassengerNotFoundException extends GlobalException {
    private static final String DEFAULT_MESSAGE = "Passenger not found";
    private static final HttpStatus HTTP_STATUS = HttpStatus.NOT_FOUND;


    public PassengerNotFoundException(Integer id) {
        super("Passenger with id : " + id + " not found", HTTP_STATUS);
    }

    public PassengerNotFoundException() {
        super(DEFAULT_MESSAGE, HTTP_STATUS);
    }

}
