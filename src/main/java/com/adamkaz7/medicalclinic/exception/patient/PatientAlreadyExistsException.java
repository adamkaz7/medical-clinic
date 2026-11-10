package com.adamkaz7.medicalclinic.exception.patient;

import com.adamkaz7.medicalclinic.exception.MedicalClinicException;
import org.springframework.http.HttpStatus;

public class PatientAlreadyExistsException extends MedicalClinicException {
    public PatientAlreadyExistsException(String email) {
        super("Patient with email " + email + " already exists", HttpStatus.CONFLICT);
    }
}
