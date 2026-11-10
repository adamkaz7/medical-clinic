package com.adamkaz7.medicalclinic.command;

import java.time.LocalDate;

public record UpdatePatientCommand(
        String idCardNo,
        String phoneNumber,
        LocalDate birthday
) {
}
