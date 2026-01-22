package com.lgambier.spaceagency.exceptions.config;

import com.lgambier.spaceagency.enums.ConflictValidators;
import com.lgambier.spaceagency.validations.ValidMissionDates;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tools.jackson.databind.exc.InvalidFormatException;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<GlobalErrorResponse> handleGlobalException(GlobalException exc, HttpServletRequest request) {

        String message = exc.getMessage();
        int status = exc.getStatus().value();

        if (status == 404) {
            switch (request.getMethod()) {
                case "DELETE" -> message += ", unable to delete";
                case "PUT" -> message += ", unable to update";
                case "PATCH" -> message += ", unable to patch";
            }
        }

        GlobalErrorResponse error = new GlobalErrorResponse(status, message, System.currentTimeMillis());

        return new ResponseEntity<>(error, exc.getStatus());
    }

    @ExceptionHandler
    public ResponseEntity<GlobalErrorResponse> handleValidationException(ConstraintViolationException exc) {
        String message = exc.getConstraintViolations().stream().map(v -> v.getPropertyPath() + " " + v.getMessage()).collect(Collectors.joining(", "));

        /*
        * Check if ConstraintViolationException is typed of any conflictValidators annotation to set HttpStatus to CONFLICT
        * */
        Set<String> constraintAnnotationTypes =
                exc.getConstraintViolations().stream()
                        .map(v ->
                                v.getConstraintDescriptor()
                                .getAnnotation()
                                .annotationType()
                                .getSimpleName())
                        .collect(Collectors.toSet());

        Set<String> conflictValidators =
                Arrays.stream(ConflictValidators.values())
                        .map(Enum::name)
                        .collect(Collectors.toSet());

        HttpStatus status =
                constraintAnnotationTypes.stream().anyMatch(conflictValidators::contains)
                        ? HttpStatus.CONFLICT
                        : HttpStatus.BAD_REQUEST;

        GlobalErrorResponse error = new GlobalErrorResponse(status.value(), message, System.currentTimeMillis());

        return new ResponseEntity<>(error, status);
    }

    @ExceptionHandler
    public ResponseEntity<GlobalErrorResponse> handleTransactionSystemException(InvalidFormatException exc) {
        String message = "Invalid Fields Format : " + exc.getMessage();

        GlobalErrorResponse error = new GlobalErrorResponse(HttpStatus.BAD_REQUEST.value(), message, System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<GlobalErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException exc) {

        GlobalErrorResponse error = new GlobalErrorResponse(HttpStatus.CONFLICT.value(), exc.getMostSpecificCause().getMessage(), System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }




}
