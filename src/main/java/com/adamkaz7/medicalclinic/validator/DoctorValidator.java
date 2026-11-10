package com.adamkaz7.medicalclinic.validator;

import com.adamkaz7.medicalclinic.command.CreateDoctorCommand;
import com.adamkaz7.medicalclinic.command.UpdateDoctorCommand;
import com.adamkaz7.medicalclinic.exception.doctor.DoctorValidationException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DoctorValidator {
    public static void validateCreateDoctorCommand(CreateDoctorCommand command) {
        if (command == null) {
            throw new DoctorValidationException("Doctor data is required");
        }
        validateDoctorData(
                command.email(),
                command.password(),
                command.firstName(),
                command.lastName(),
                command.specialization()
        );
    }

    public static void validateUpdateDoctorCommand(UpdateDoctorCommand command) {
        if (command == null) {
            throw new DoctorValidationException("Doctor data is required");
        }
        validateDoctorData(
                command.email(),
                command.password(),
                command.firstName(),
                command.lastName(),
                command.specialization()
        );
    }

    public static void validateMedicalFacilityAssignment(boolean alreadyAssigned) {
        if (alreadyAssigned) {
            throw new DoctorValidationException("Doctor is already assigned to this medical facility");
        }
    }

    public static void validatePassword(String password) {
        validateRequiredField(password, "Password");
    }

    private static void validateDoctorData(
            String email,
            String password,
            String firstName,
            String lastName,
            String specialization
    ) {
        validateRequiredField(email, "Email");
        validatePassword(password);
        validateRequiredField(firstName, "First name");
        validateRequiredField(lastName, "Last name");
        validateRequiredField(specialization, "Specialization");
    }

    private static void validateRequiredField(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new DoctorValidationException(fieldName + " is required");
        }
    }
}
