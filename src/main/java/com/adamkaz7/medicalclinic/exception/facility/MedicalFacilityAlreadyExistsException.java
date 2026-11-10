package com.adamkaz7.medicalclinic.exception.facility;

import com.adamkaz7.medicalclinic.exception.MedicalClinicException;
import org.springframework.http.HttpStatus;

public class MedicalFacilityAlreadyExistsException extends MedicalClinicException {
    public MedicalFacilityAlreadyExistsException(String name) {
        super("Medical facility with name " + name + " already exists", HttpStatus.CONFLICT);
    }
}
