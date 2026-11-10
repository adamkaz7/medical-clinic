package com.adamkaz7.medicalclinic.command;

import jakarta.validation.constraints.NotBlank;

public record UpdateMedicalFacilityCommand(
        @NotBlank String name,
        @NotBlank String city,
        @NotBlank String postalCode,
        @NotBlank String street,
        @NotBlank String buildingNumber
) {
}
