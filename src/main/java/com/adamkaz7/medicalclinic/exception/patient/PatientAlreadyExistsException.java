package com.adamkaz7.medicalclinic.exception.patient;

import com.adamkaz7.medicalclinic.exception.MedicalClinicException;
import org.springframework.http.HttpStatus;

public class PatientAlreadyExistsException extends MedicalClinicException {
    public PatientAlreadyExistsException(String idCardNo) {
        super("Patient with id card number " + idCardNo + " already exists", HttpStatus.CONFLICT);
    }
}
