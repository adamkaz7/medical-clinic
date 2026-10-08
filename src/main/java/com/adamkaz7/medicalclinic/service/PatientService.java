package com.adamkaz7.medicalclinic.service;

import com.adamkaz7.medicalclinic.command.CreatePatientCommand;
import com.adamkaz7.medicalclinic.dto.PatientDto;
import com.adamkaz7.medicalclinic.exception.PatientAlreadyExistsException;
import com.adamkaz7.medicalclinic.exception.PatientNotFoundException;
import com.adamkaz7.medicalclinic.mapper.PatientMapper;
import com.adamkaz7.medicalclinic.model.Patient;
import com.adamkaz7.medicalclinic.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    public List<PatientDto> getAllPatients() {
        return patientRepository.findAll()
                .stream()
                .map(patientMapper::toDto)
                .toList();
    }

    public PatientDto getPatientByEmail(String email) {
        Patient patient = findPatientByEmail(email);
        return patientMapper.toDto(patient);
    }

    public PatientDto addPatient(CreatePatientCommand command) {
        Patient patient = patientMapper.toPatient(command);
        validatePatient(patient);
        return patientRepository.add(patient)
                .map(patientMapper::toDto)
                .orElseThrow(() -> new PatientAlreadyExistsException(patient.getEmail()));
    }

    public void deletePatientByEmail(String email) {
        validateEmail(email);
        if (!patientRepository.deleteByEmail(email)) {
            throw new PatientNotFoundException(email);
        }
    }

    public PatientDto updatePatientByEmail(String email, CreatePatientCommand command) {
        Patient patient = patientMapper.toPatient(command);
        validateEmail(email);
        validatePatient(patient);
        if (!email.equals(patient.getEmail())) {
            throw new IllegalArgumentException("Email cannot be changed when updating a patient");
        }
        return patientRepository.update(patient)
                .map(patientMapper::toDto)
                .orElseThrow(() -> new PatientNotFoundException(email));
    }

    public void changePatientPassword(String email, String newPassword) {
        Patient patient = findPatientByEmail(email);
        Patient updatedPatient = patient.withPassword(newPassword);
        patientRepository.update(updatedPatient)
                .orElseThrow(() -> new PatientNotFoundException(email));
    }

    private Patient findPatientByEmail(String email) {
        validateEmail(email);
        return patientRepository.findByEmail(email)
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
