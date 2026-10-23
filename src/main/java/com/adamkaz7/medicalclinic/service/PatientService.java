package com.adamkaz7.medicalclinic.service;

import com.adamkaz7.medicalclinic.command.CreatePatientCommand;
import com.adamkaz7.medicalclinic.command.UpdatePatientCommand;
import com.adamkaz7.medicalclinic.dto.PatientDto;
import com.adamkaz7.medicalclinic.exception.PatientAlreadyExistsException;
import com.adamkaz7.medicalclinic.exception.PatientNotFoundException;
import com.adamkaz7.medicalclinic.mapper.PatientMapper;
import com.adamkaz7.medicalclinic.model.Patient;
import com.adamkaz7.medicalclinic.repository.PatientRepository;
import com.adamkaz7.medicalclinic.validator.PatientValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;
    private final PatientValidator patientValidator;

    public List<PatientDto> getAllPatients() {
        return patientRepository.findAll().stream()
                .map(patientMapper::toDto)
                .toList();
    }

    public PatientDto getPatientByEmail(String email) {
        Patient patient = findPatientByEmail(email);
        return patientMapper.toDto(patient);
    }

    public PatientDto addPatient(CreatePatientCommand command) {
        Patient patient = patientMapper.toEntity(command);
        patientValidator.validatePatient(patient);
        if (patientRepository.existsByEmail(patient.getEmail())) {
            throw new PatientAlreadyExistsException(patient.getEmail());
        }
        Patient savedPatient = patientRepository.save(patient);
        return patientMapper.toDto(patient);
    }

    public void deletePatientByEmail(String email) {
        Patient patient = findPatientByEmail(email);
        patientRepository.delete(patient);
    }

    public PatientDto updatePatientByEmail(String email, UpdatePatientCommand command) {
        Patient updatedPatient = patientMapper.toEntity(command);
        patientValidator.validateUpdatedPatient(email, updatedPatient);
        Patient patient = findPatientByEmail(email);
        patient.updatePatient(updatedPatient);
        Patient savedPatient = patientRepository.save(patient);
        return patientMapper.toDto(savedPatient);
    }

    public void changePatientPassword(String email, String newPassword) {
        Patient patient = findPatientByEmail(email);
        Patient updatedPatient = patient.withPassword(newPassword);
        patientRepository.save(updatedPatient);
    }

    private Patient findPatientByEmail(String email) {
        patientValidator.validateEmail(email);
        return patientRepository.findByEmail(email)
                .orElseThrow(() -> new PatientNotFoundException(email));
    }
}
