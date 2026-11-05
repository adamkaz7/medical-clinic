package com.adamkaz7.medicalclinic.dto;

import java.util.Set;

public record DoctorDto(
        Long id,
        String email,
        String firstName,
        String lastName,
        String specialization,
        Set<MedicalFacilityDto> medicalFacilities
) {
}
