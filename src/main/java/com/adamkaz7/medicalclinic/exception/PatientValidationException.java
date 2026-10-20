package com.adamkaz7.medicalclinic.exception;

import org.springframework.http.HttpStatus;

public class PatientValidationException extends MedicalClinicException {
    public PatientValidationException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
