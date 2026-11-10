package com.adamkaz7.medicalclinic.validator;

import com.adamkaz7.medicalclinic.command.CreateMedicalFacilityCommand;
import com.adamkaz7.medicalclinic.command.UpdateMedicalFacilityCommand;
import com.adamkaz7.medicalclinic.exception.facility.MedicalFacilityValidationException;
import org.springframework.stereotype.Component;

@Component
public class MedicalFacilityValidator {
    public void validateMedicalFacilityData(CreateMedicalFacilityCommand command) {
        if (command == null) {
            throw new MedicalFacilityValidationException("Medical Facility data is required");
        }
        validateRequiredField(command.name(), "Name");
        validateRequiredField(command.city(), "City");
        validateRequiredField(command.postalCode(), "Postal code");
        validateRequiredField(command.street(), "Street");
        validateRequiredField(command.buildingNumber(), "Building number");
    }

    public void validateUpdateMedicalFacilityCommand(UpdateMedicalFacilityCommand command) {
        if (command == null) {
            throw new MedicalFacilityValidationException("Medical Facility data is required");
        }
        validateMedicalFacilityData(
                command.name(),
                command.city(),
                command.postalCode(),
                command.street(),
                command.buildingNumber()
        );
    }

    private void validateMedicalFacilityData(
            String name,
            String city,
            String postalCode,
            String street,
            String buildingNumber
    ) {
        validateRequiredField(name, "Name");
        validateRequiredField(city, "City");
        validateRequiredField(postalCode, "Postal code");
        validateRequiredField(street, "Street");
        validateRequiredField(buildingNumber, "Building number");
    }

    private void validateRequiredField(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new MedicalFacilityValidationException(fieldName + " is required");
        }
    }
}
