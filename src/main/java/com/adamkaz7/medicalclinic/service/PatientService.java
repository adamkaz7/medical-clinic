package com.adamkaz7.medicalclinic.service;

import com.adamkaz7.medicalclinic.exception.PatientAlreadyExistsException;
import com.adamkaz7.medicalclinic.exception.PatientNotFoundException;
import com.adamkaz7.medicalclinic.model.Patient;
import com.adamkaz7.medicalclinic.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {
    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Patient getPatientByEmail(String email) {
        validateEmail(email);

        return patientRepository.findByEmail(email)
                .orElseThrow(() -> new PatientNotFoundException(email));
    }

    public Patient addPatient(Patient patient) {
        validatePatient(patient);

        boolean added = patientRepository.add(patient);

        if (!added) {
            throw new PatientAlreadyExistsException(patient.getEmail());
        }

        return patient;
    }

    public void deletePatientByEmail(String email) {
        validateEmail(email);

        boolean deleted = patientRepository.deleteByEmail(email);

        if (!deleted) {
            throw new PatientNotFoundException(email);
        }
    }

    public Patient updatePatientByEmail(String email, Patient patient) {
        validateEmail(email);
        validatePatient(patient);

        if (!email.equals(patient.getEmail())) {
            throw new IllegalArgumentException("Email cannot be changed when updating a patient");
        }

        boolean updated = patientRepository.update(patient);

        if (!updated) {
            throw new PatientNotFoundException(email);
        }

        return patient;
    }

    private void validatePatient(Patient patient) {
        if (patient == null) {
            throw new IllegalArgumentException("Patient data is required");
        }

        validateEmail(patient.getEmail());
    }

    private void validateEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }
    }
}
