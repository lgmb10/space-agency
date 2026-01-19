package com.lgambier.spaceagency.exceptions.mission;

import com.lgambier.spaceagency.exceptions.config.GlobalException;
import org.springframework.http.HttpStatus;


public class MissionNotFoundException extends GlobalException {
    private static final String DEFAULT_MESSAGE = "Mission not found";
    private static final HttpStatus HTTP_STATUS = HttpStatus.NOT_FOUND;


    public MissionNotFoundException(Integer id) {
        super("Mission with id : " + id + " not found", HTTP_STATUS);
    }

    public MissionNotFoundException() {
        super(DEFAULT_MESSAGE, HTTP_STATUS);
    }

}
