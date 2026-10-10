package com.adamkaz7.medicalclinic.service;

import com.adamkaz7.medicalclinic.exception.PatientAlreadyExistsException;
import com.adamkaz7.medicalclinic.exception.PatientNotFoundException;
import com.adamkaz7.medicalclinic.model.Patient;
import com.adamkaz7.medicalclinic.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository patientRepository;

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
        return patientRepository.add(patient)
                .orElseThrow(() -> new PatientAlreadyExistsException(patient.getEmail()));
    }

    public void deletePatientByEmail(String email) {
        validateEmail(email);
        if (!patientRepository.deleteByEmail(email)) {
            throw new PatientNotFoundException(email);
        }
    }

    public Patient updatePatientByEmail(String email, Patient patient) {
        validateEmail(email);
        validatePatient(patient);
        if (!email.equals(patient.getEmail())) {
            throw new IllegalArgumentException("Email cannot be changed when updating a patient");
        }
        return patientRepository.update(patient)
                .orElseThrow(() -> new PatientNotFoundException(email));
    }

    public Patient changePatientPassword(String email, String newPassword) {
        Patient patient = getPatientByEmail(email);
        Patient updatedPatient = patient.withPassword(newPassword);
        return patientRepository.update(patient)
                .orElseThrow(() -> new PatientNotFoundException(email));
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
