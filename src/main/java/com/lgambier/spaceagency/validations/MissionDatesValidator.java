package com.lgambier.spaceagency.validations;

import com.lgambier.spaceagency.models.Mission;
import com.lgambier.spaceagency.models.Ship;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class MissionDatesValidator implements ConstraintValidator<ValidMissionDates, Mission> {

    @Override
    public boolean isValid(Mission mission, ConstraintValidatorContext constraintValidatorContext) {
        if(mission.getDepartureDate() == null || mission.getArrivalDate() == null) return false;

        return !mission.getDepartureDate().isAfter(mission.getArrivalDate());
    }
}
