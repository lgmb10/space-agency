package com.lgambier.spaceagency.validations;

import com.lgambier.spaceagency.enums.ShipStatus;
import com.lgambier.spaceagency.exceptions.ship.ShipNotFoundException;
import com.lgambier.spaceagency.models.Ship;
import com.lgambier.spaceagency.services.ShipService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
public class ShipStatusIsActiveValidator implements ConstraintValidator<ShipStatusIsActive, Ship> {

    @Override
    public boolean isValid(Ship ship, ConstraintValidatorContext context) {
        return ship.getStatus() == ShipStatus.ACTIVE;
    }
}
