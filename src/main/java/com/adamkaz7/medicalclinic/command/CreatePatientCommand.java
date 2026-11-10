package com.adamkaz7.medicalclinic.command;

import java.time.LocalDate;

public record CreatePatientCommand(
        Long userId,
        String idCardNo,
        String phoneNumber,
        LocalDate birthday
) {
}