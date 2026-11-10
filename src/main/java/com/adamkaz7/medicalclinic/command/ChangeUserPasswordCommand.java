package com.adamkaz7.medicalclinic.command;

import jakarta.validation.constraints.NotBlank;

public record ChangeUserPasswordCommand(
        @NotBlank String password
) {
}
