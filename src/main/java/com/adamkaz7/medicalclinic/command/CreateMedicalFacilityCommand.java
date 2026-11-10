package com.adamkaz7.medicalclinic.command;

public record CreateMedicalFacilityCommand(
        String name,
        String city,
        String postalCode,
        String street,
        String buildingNumber
) {
}
