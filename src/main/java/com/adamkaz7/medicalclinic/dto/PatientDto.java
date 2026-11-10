package com.adamkaz7.medicalclinic.dto;

import java.time.LocalDate;

public record PatientDto(
        String idCardNo,
        String phoneNumber,
        LocalDate birthday,
        UserDto user
) {
}
