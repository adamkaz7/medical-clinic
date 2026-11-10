package com.adamkaz7.medicalclinic.validator;

import com.adamkaz7.medicalclinic.exception.patient.PatientValidationException;
import org.springframework.stereotype.Component;

@Component
public class PatientValidator {
    public void validateEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new PatientValidationException("Email is required");
        }
    }

    public void validateIdCardNoUnchanged(String currentIdCardNo, String newIdCardNo) {
        if (!currentIdCardNo.equals(newIdCardNo)) {
            throw new PatientValidationException("Id card number cannot be changed when updating a patient");
        }
    }

    public void validateUserAssignment(boolean userAlreadyAssigned) {
        if (userAlreadyAssigned) {
            throw new PatientValidationException("User is already assigned to a patient");
        }
    }
}
