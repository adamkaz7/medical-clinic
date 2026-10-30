package com.adamkaz7.medicalclinic.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends MedicalClinicException {
    public UserNotFoundException(Long id) {
        super("User with id " + id + " was not found", HttpStatus.NOT_FOUND);
    }
}
