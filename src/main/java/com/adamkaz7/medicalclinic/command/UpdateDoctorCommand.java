package com.adamkaz7.medicalclinic.command;

public record UpdateDoctorCommand(
        String email,
        String password,
        String firstName,
        String lastName,
        String specialization
) {
}
