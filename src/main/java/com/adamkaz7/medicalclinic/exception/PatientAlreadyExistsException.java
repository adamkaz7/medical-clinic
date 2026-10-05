package com.adamkaz7.medicalclinic.exception;

public class PatientAlreadyExistsException extends RuntimeException {
    public PatientAlreadyExistsException(String email) {
        super("Patient with email " + email + " already exists");
    }
}
