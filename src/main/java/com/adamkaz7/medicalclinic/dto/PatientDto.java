package com.adamkaz7.medicalclinic.dto;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record PatientDto(
        String email,
        String idCardNo,
        String firstName,
        String lastName,
        String phoneNumber,
        LocalDate birthday
) {
}
