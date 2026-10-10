package com.adamkaz7.medicalclinic.dto;

import com.adamkaz7.medicalclinic.model.Patient;

import java.time.LocalDate;

public record CreatePatientCommand(
        String email,
        String password,
        String idCardNo,
        String firstName,
        String lastName,
        String phoneNumber,
        LocalDate birthday
) {
    public Patient toPatient() {
        return new Patient(
                email,
                password,
                idCardNo,
                firstName,
                lastName,
                phoneNumber,
                birthday
        );
    }

    @Override
    public String toString() {
        return "CreatePatientCommand{email=" + email + '}';
    }
}
