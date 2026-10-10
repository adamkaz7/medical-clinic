package com.adamkaz7.medicalclinic.validator;

import com.adamkaz7.medicalclinic.model.Patient;
import org.springframework.stereotype.Component;

@Component
public class PatientValidator {
    public void validatePatient(Patient patient) {
        if (patient == null) {
            throw new IllegalArgumentException("Patient data is required");
        }
        validateEmail(patient.getEmail());
    }

    public void validateEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }
    }

    public void validateUpdatedPatient(String email, Patient patient) {
        validateEmail(email);
        validatePatient(patient);
        validateEmailUnchanged(email, patient);
    }

    private void validateEmailUnchanged(String email, Patient patient) {
        if (!email.equals(patient.getEmail())) {
            throw new IllegalArgumentException("Email cannot be changed when updating a patient");
        }
    }
}
