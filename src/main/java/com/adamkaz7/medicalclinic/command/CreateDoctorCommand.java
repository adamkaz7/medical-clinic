package com.adamkaz7.medicalclinic.command;

public record CreateDoctorCommand(
        String email,
        String password,
        String firstName,
        String lastName,
        String specialization
) {
}
