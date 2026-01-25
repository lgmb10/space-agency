package com.lgambier.spaceagency.exceptions.mission;

import com.lgambier.spaceagency.exceptions.config.GlobalException;
import org.springframework.http.HttpStatus;

public class MissionPassengerAlreadyAffectedToGivenMissionException extends GlobalException {
    private static final String DEFAULT_MESSAGE = "This passenger is already affected to mission";
    private static final HttpStatus HTTP_STATUS = HttpStatus.CONFLICT;

    public MissionPassengerAlreadyAffectedToGivenMissionException(Integer missionId) {
        super(DEFAULT_MESSAGE+ " with id : "+missionId, HTTP_STATUS);
    }

    public MissionPassengerAlreadyAffectedToGivenMissionException() {
        super(DEFAULT_MESSAGE, HTTP_STATUS);
    }
}
