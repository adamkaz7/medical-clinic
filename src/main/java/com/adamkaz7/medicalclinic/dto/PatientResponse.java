package com.adamkaz7.medicalclinic.dto;

import com.adamkaz7.medicalclinic.model.Patient;

import java.time.LocalDate;

public record PatientResponse(
        String email,
        String idCardNo,
        String firstName,
        String lastName,
        String phoneNumber,
        LocalDate birthday
) {
    public static PatientResponse from(Patient patient) {
        return new PatientResponse(
                patient.getEmail(),
                patient.getIdCardNo(),
                patient.getFirstName(),
                patient.getLastName(),
                patient.getPhoneNumber(),
                patient.getBirthday()
        );
    }
}
