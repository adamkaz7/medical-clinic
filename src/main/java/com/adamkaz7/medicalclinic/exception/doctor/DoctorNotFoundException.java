package com.adamkaz7.medicalclinic.exception.doctor;

import com.adamkaz7.medicalclinic.exception.MedicalClinicException;
import org.springframework.http.HttpStatus;

public class DoctorNotFoundException extends MedicalClinicException {
    public DoctorNotFoundException(Long doctorId) {
        super("Doctor with id " + doctorId + " was not found", HttpStatus.NOT_FOUND);
    }
}
