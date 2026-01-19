package com.lgambier.spaceagency.exceptions.config;

public record GlobalErrorResponse(int status, String message, long timeStamp) {
}
