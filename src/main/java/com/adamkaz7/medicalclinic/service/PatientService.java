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
        return patientRepository.add(patient)
                .map(patientMapper::toDto)
                .orElseThrow(() -> new PatientAlreadyExistsException(patient.getEmail()));
    }

    public void deletePatientByEmail(String email) {
        patientValidator.validateEmail(email);
        if (!patientRepository.deleteByEmail(email)) {
            throw new PatientNotFoundException(email);
        }
    }

    public PatientDto updatePatientByEmail(String email, UpdatePatientCommand command) {
        Patient patient = patientMapper.toEntity(command);
        patientValidator.validateUpdatedPatient(email, patient);
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
        patientValidator.validateEmail(email);
        return patientRepository.findByEmail(email)
                .orElseThrow(() -> new PatientNotFoundException(email));
    }
}
