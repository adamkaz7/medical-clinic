package com.adamkaz7.medicalclinic.exception.appointment;

import com.adamkaz7.medicalclinic.exception.MedicalClinicException;
import org.springframework.http.HttpStatus;

public class AppointmentNotFoundException extends MedicalClinicException {
    public AppointmentNotFoundException(Long appointmentId) {
        super("Appointment with id " + appointmentId + " was not found", HttpStatus.NOT_FOUND);
    }
}
