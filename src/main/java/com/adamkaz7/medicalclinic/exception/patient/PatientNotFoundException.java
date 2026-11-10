package com.adamkaz7.medicalclinic.exception.patient;

import com.adamkaz7.medicalclinic.exception.MedicalClinicException;
import org.springframework.http.HttpStatus;

public class PatientNotFoundException extends MedicalClinicException {
    public PatientNotFoundException(String email) {
        super("Patient with email " + email + " was not found", HttpStatus.NOT_FOUND);
    }

    public PatientNotFoundException(Long patientId) {
        super("Patient with id " + patientId + " was not found",HttpStatus.NOT_FOUND);
    }
}
