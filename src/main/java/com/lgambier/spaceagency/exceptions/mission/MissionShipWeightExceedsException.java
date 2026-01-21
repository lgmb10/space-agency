package com.lgambier.spaceagency.exceptions.mission;

import com.lgambier.spaceagency.exceptions.config.GlobalException;
import com.lgambier.spaceagency.models.Ship;
import org.springframework.http.HttpStatus;

public class MissionShipWeightExceedsException extends GlobalException {
    private static final String DEFAULT_MESSAGE = "Weight capacity exceeds, ship cannot handle this passenger";
    private static final HttpStatus HTTP_STATUS = HttpStatus.CONFLICT;

    public MissionShipWeightExceedsException() {
        super(DEFAULT_MESSAGE, HTTP_STATUS);
    }
}
