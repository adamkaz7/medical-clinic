package com.adamkaz7.medicalclinic.validator;

import com.adamkaz7.medicalclinic.command.CreatePatientCommand;
import com.adamkaz7.medicalclinic.command.UpdatePatientCommand;
import com.adamkaz7.medicalclinic.exception.patient.PatientValidationException;
import com.adamkaz7.medicalclinic.model.Patient;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class PatientValidator {
    public void validateCreatePatientCommand(CreatePatientCommand command) {
        if (command == null) {
            throw new PatientValidationException("Patient data is required");
        }
        if (command.userId() == null) {
            throw new PatientValidationException("User id is required");
        }
        validatePatientData(
                command.idCardNo(),
                command.phoneNumber(),
                command.birthday()
        );
    }

    public void validateUpdatePatientCommand(String email, UpdatePatientCommand command) {
        if (command == null) {
            throw new PatientValidationException("Patient data is required");
        }
        validateEmail(email);
        validatePatientData(
                command.idCardNo(),
                command.phoneNumber(),
                command.birthday()
        );
    }

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

    private void validatePatientData(String idCardNo, String phoneNumber, LocalDate birthday) {
        validateRequiredField(idCardNo, "ID card number");
        validateRequiredField(phoneNumber, "Phone number");
        if (birthday == null) {
            throw new PatientValidationException("Birthday is required");
        }
    }

    private void validateRequiredField(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new PatientValidationException(fieldName + " is required");
        }
    }
}
