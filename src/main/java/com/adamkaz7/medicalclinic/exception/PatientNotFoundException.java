package com.adamkaz7.medicalclinic.exception;

public class PatientNotFoundException extends RuntimeException {
    public PatientNotFoundException(String email) {
        super("Patient with email " + email + " was not found");
    }
}
