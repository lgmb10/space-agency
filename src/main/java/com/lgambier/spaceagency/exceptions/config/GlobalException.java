package com.lgambier.spaceagency.exceptions.config;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class GlobalException extends RuntimeException {
    public static final HttpStatus DEFAULT_STATUS = HttpStatus.INTERNAL_SERVER_ERROR;
    private final HttpStatus status;

    public GlobalException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public GlobalException(String message) {
        super(message);
        this.status = DEFAULT_STATUS;
    }

    public GlobalException() {
        super("An Error Occurred");
        this.status = DEFAULT_STATUS;
    }

}

