package com.lgambier.spaceagency.exceptions.mission;

import com.lgambier.spaceagency.enums.MissionStatus;
import com.lgambier.spaceagency.exceptions.config.GlobalException;
import org.springframework.http.HttpStatus;

public class MissionTransitionException extends GlobalException {
    private static final String DEFAULT_MESSAGE = "Transition between old and new mission status not allowed";
    private static final HttpStatus HTTP_STATUS = HttpStatus.CONFLICT;

    public MissionTransitionException(MissionStatus oldStatus, MissionStatus newStatus, String specificCause) {
        super("Mission status transition from : " + oldStatus + " to "+newStatus+" not allowed. "+specificCause, HTTP_STATUS);
    }

    public MissionTransitionException(MissionStatus oldStatus, MissionStatus newStatus) {
        super("Mission status transition from : " + oldStatus + " to "+newStatus+" not allowed", HTTP_STATUS);
    }

    public MissionTransitionException() {
        super(DEFAULT_MESSAGE, HTTP_STATUS);
    }
}
