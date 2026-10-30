package com.adamkaz7.medicalclinic.validator;

import com.adamkaz7.medicalclinic.command.CreatePatientCommand;
import com.adamkaz7.medicalclinic.command.UpdatePatientCommand;
import com.adamkaz7.medicalclinic.exception.PatientValidationException;
import org.springframework.stereotype.Component;

@Component
public class PatientValidator {
    public void validateCreatePatientCommand(CreatePatientCommand command) {
        if (command == null) {
            throw new PatientValidationException("Patient data is required");
        }
        validateEmail(command.email());
    }

    public void validateUpdatePatientCommand(String email, UpdatePatientCommand command) {
        if (command == null) {
            throw new PatientValidationException("Patient data is required");
        }
        validateEmail(email);
        validateEmail(command.email());
        if (!email.equals(command.email())) {
            throw new PatientValidationException("Email cannot be changed when updating a patient");
        }
        validatePatientData(command);
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

    private void validatePatientData(UpdatePatientCommand command) {
        validatePassword(command.password());
        if (command.idCardNo() == null) {
            throw new PatientValidationException("ID card number is required");
        }
        if (command.firstName() == null) {
            throw new PatientValidationException("First name is required");
        }
        if (command.lastName() == null) {
            throw new PatientValidationException("Last name is required");
        }
        if (command.phoneNumber() == null) {
            throw new PatientValidationException("Phone number is required");
        }
        if (command.birthday() == null) {
            throw new PatientValidationException("Birthday is required");
        }
    }

    public void validatePassword(String password) {
        if (password == null || password.isBlank()) {
            throw new PatientValidationException("Password is required");
        }
    }
}
