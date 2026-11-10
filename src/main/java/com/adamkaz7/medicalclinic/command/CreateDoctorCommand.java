package com.adamkaz7.medicalclinic.command;

import jakarta.validation.constraints.NotBlank;

public record CreateDoctorCommand(
        @NotBlank String email,
        @NotBlank String password,
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotBlank String specialization
) {
}
