package com.adamkaz7.medicalclinic.exception.facility;

import com.adamkaz7.medicalclinic.exception.MedicalClinicException;
import org.springframework.http.HttpStatus;

public class MedicalFacilityNotFoundException extends MedicalClinicException {
    public MedicalFacilityNotFoundException(Long facilityId) {
        super("Medical facility with id " + facilityId + " was not found", HttpStatus.NOT_FOUND);
    }
}
