package com.adamkaz7.medicalclinic.dto;

public record MedicalFacilityDto(
        Long id,
        String name,
        String city,
        String postalCode,
        String street,
        String buildingNumber
) {
}
