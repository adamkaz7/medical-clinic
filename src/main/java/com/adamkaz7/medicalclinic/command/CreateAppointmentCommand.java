package com.adamkaz7.medicalclinic.command;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreateAppointmentCommand(
        @NotNull Long doctorId,
        @NotNull @FutureOrPresent LocalDateTime startTime,
        @NotNull @FutureOrPresent LocalDateTime endTime
) {
}
