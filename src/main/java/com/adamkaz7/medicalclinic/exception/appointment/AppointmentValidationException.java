package com.adamkaz7.medicalclinic.exception.appointment;

import com.adamkaz7.medicalclinic.exception.MedicalClinicException;
import org.springframework.http.HttpStatus;

public class AppointmentValidationException extends MedicalClinicException {
    public AppointmentValidationException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
