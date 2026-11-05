package com.adamkaz7.medicalclinic.exception.appointment;

import com.adamkaz7.medicalclinic.exception.MedicalClinicException;
import org.springframework.http.HttpStatus;

public class AppointmentAlreadyExistsException extends MedicalClinicException {
    public AppointmentAlreadyExistsException(Long doctorId) {
        super("Doctor with id " + doctorId + " already has an appointment in this time range", HttpStatus.CONFLICT);
    }
}
