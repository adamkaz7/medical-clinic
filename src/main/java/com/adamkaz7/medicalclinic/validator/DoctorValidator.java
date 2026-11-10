package com.adamkaz7.medicalclinic.validator;

import com.adamkaz7.medicalclinic.exception.doctor.DoctorValidationException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DoctorValidator {
    public static void validateMedicalFacilityAssignment(boolean alreadyAssigned) {
        if (alreadyAssigned) {
            throw new DoctorValidationException("Doctor is already assigned to this medical facility");
        }
    }
}
