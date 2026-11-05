package com.adamkaz7.medicalclinic.exception.facility;

import com.adamkaz7.medicalclinic.exception.MedicalClinicException;
import org.springframework.http.HttpStatus;

public class MedicalFacilityValidationException extends MedicalClinicException {
    public MedicalFacilityValidationException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
