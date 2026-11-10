package com.adamkaz7.medicalclinic.command;

import jakarta.validation.constraints.NotBlank;

public record ChangeDoctorPasswordCommand(
        @NotBlank String password
) {
}
