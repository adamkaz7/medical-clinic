package com.adamkaz7.medicalclinic.exception;

import org.springframework.http.HttpStatus;

public class UserValidationException extends MedicalClinicException {
    public UserValidationException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
