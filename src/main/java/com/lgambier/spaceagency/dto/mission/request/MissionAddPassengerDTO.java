package com.lgambier.spaceagency.dto.mission.request;

import jakarta.validation.constraints.NotNull;

public record MissionAddPassengerDTO(@NotNull Integer passengerId) {
}
