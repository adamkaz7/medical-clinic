package com.adamkaz7.medicalclinic.exception.doctor;

import com.adamkaz7.medicalclinic.exception.MedicalClinicException;
import org.springframework.http.HttpStatus;

public class DoctorAlreadyExistsException extends MedicalClinicException {
    public DoctorAlreadyExistsException(String email) {
        super("Doctor with email " + email + " already exists", HttpStatus.CONFLICT);
    }
}
