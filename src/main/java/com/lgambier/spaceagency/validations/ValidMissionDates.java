package com.lgambier.spaceagency.validations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MissionDatesValidator.class)
public @interface ValidMissionDates {
    String message() default "Departure date must be before arrival date";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
