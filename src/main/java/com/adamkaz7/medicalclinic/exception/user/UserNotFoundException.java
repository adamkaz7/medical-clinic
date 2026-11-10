package com.adamkaz7.medicalclinic.exception.user;

import com.adamkaz7.medicalclinic.exception.MedicalClinicException;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends MedicalClinicException {
    public UserNotFoundException(Long id) {
        super("User with id " + id + " was not found", HttpStatus.NOT_FOUND);
    }
}
