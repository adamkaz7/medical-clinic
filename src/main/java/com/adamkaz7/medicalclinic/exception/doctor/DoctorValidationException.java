package com.adamkaz7.medicalclinic.exception.doctor;

import com.adamkaz7.medicalclinic.exception.MedicalClinicException;
import org.springframework.http.HttpStatus;

public class DoctorValidationException extends MedicalClinicException {
    public DoctorValidationException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
