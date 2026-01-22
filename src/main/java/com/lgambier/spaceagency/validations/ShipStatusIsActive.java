package com.lgambier.spaceagency.validations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ShipStatusIsActiveValidator.class)
public @interface ShipStatusIsActive {
    String message() default "Ship status must be ACTIVE";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

