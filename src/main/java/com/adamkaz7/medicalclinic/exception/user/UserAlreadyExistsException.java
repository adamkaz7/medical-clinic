package com.adamkaz7.medicalclinic.exception.user;

import com.adamkaz7.medicalclinic.exception.MedicalClinicException;
import org.springframework.http.HttpStatus;

public class UserAlreadyExistsException extends MedicalClinicException {
    public UserAlreadyExistsException(String email) {
        super("User with email " + email + " already exists", HttpStatus.CONFLICT);
    }
}
