package com.lgambier.spaceagency.exceptions.ship;

import com.lgambier.spaceagency.exceptions.config.GlobalException;
import org.springframework.http.HttpStatus;


public class ShipNotFoundException extends GlobalException {
    private static final String DEFAULT_MESSAGE = "Ship not found";
    private static final HttpStatus HTTP_STATUS = HttpStatus.NOT_FOUND;


    public ShipNotFoundException(Integer id) {
        super("Ship with id : " + id + " not found", HTTP_STATUS);
    }

    public ShipNotFoundException() {
        super(DEFAULT_MESSAGE, HTTP_STATUS);
    }

}
