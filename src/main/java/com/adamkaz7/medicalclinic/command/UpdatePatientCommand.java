package com.adamkaz7.medicalclinic.command;

import java.time.LocalDate;

public record UpdatePatientCommand(
        String email,
        String password,
        String idCardNo,
        String firstName,
        String lastName,
        String phoneNumber,
        LocalDate birthday
) {
}
