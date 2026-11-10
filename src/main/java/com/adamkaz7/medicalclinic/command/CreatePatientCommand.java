package com.adamkaz7.medicalclinic.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CreatePatientCommand(
        @NotNull Long userId,
        @NotBlank String idCardNo,
        @NotBlank String phoneNumber,
        @NotNull LocalDate birthday
) {
}