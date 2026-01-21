package com.lgambier.spaceagency.exceptions.ship;

import com.lgambier.spaceagency.exceptions.config.GlobalException;
import org.springframework.http.HttpStatus;

public class ShipCannotDeleteMissionPlannedOrInProgressAssociatedException extends GlobalException {
    private static final String DEFAULT_MESSAGE = "Cannot delete this ship because it’s associated with a mission that is currently in progress or planned.";
    private static final HttpStatus HTTP_STATUS = HttpStatus.CONFLICT;

    public ShipCannotDeleteMissionPlannedOrInProgressAssociatedException(String message) {
        super(message);
    }

    public ShipCannotDeleteMissionPlannedOrInProgressAssociatedException() {
        super(DEFAULT_MESSAGE, HTTP_STATUS);
    }
}
