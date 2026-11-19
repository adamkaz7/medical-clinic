package com.adamkaz7.medicalclinic.service;

import com.adamkaz7.medicalclinic.command.CreatePatientCommand;
import com.adamkaz7.medicalclinic.command.UpdatePatientCommand;
import com.adamkaz7.medicalclinic.dto.PatientDto;
import com.adamkaz7.medicalclinic.exception.patient.PatientAlreadyExistsException;
import com.adamkaz7.medicalclinic.exception.patient.PatientNotFoundException;
import com.adamkaz7.medicalclinic.exception.user.UserNotFoundException;
import com.adamkaz7.medicalclinic.mapper.PatientMapper;
import com.adamkaz7.medicalclinic.model.Patient;
import com.adamkaz7.medicalclinic.model.User;
import com.adamkaz7.medicalclinic.repository.PatientRepository;
import com.adamkaz7.medicalclinic.repository.UserRepository;
import com.adamkaz7.medicalclinic.validator.PatientValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Pageable;

@Service
@RequiredArgsConstructor
@Slf4j
public class PatientService {
    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;
    private final PatientValidator patientValidator;
    private final UserRepository userRepository;

    @Transactional
    public Page<PatientDto> getAllPatients(Pageable pageable) {
        return patientRepository.findAll(pageable)
                .map(patientMapper::toDto);
    }

    @Transactional
    public PatientDto getPatientByEmail(String email) {
        Patient patient = findPatientByEmail(email);
        return patientMapper.toDto(patient);
    }

    @Transactional
    public PatientDto addPatient(CreatePatientCommand command) {
        User user = userRepository.findById(command.userId())
                .orElseThrow(() -> new UserNotFoundException(command.userId()));
        patientValidator.validateUserAssignment(
                patientRepository.existsByUser_Id(command.userId())
        );
        if (patientRepository.existsByIdCardNo(command.idCardNo())) {
            throw new PatientAlreadyExistsException(command.idCardNo());
        }
        log.info("Creating patient profile: userId={}", command.userId());
        Patient patient = patientMapper.toEntity(command);
        patient.assignUser(user);
        patient = patientRepository.save(patient);
        return patientMapper.toDto(patient);
    }

    @Transactional
    public void deletePatientByEmail(String email) {
        Patient patient = findPatientByEmail(email);
        log.info("Deleting patient profile: patientId={}", patient.getId());
        patientRepository.delete(patient);
    }

    @Transactional
    public PatientDto updatePatientByEmail(String email, UpdatePatientCommand command) {
        Patient patient = findPatientByEmail(email);
        patientValidator.validateIdCardNoUnchanged(patient.getIdCardNo(), command.idCardNo());
        log.info("Updating patient profile: patientId={}", patient.getId());
        patient.updatePatient(command);
        patient = patientRepository.save(patient);
        return patientMapper.toDto(patient);
    }

    private Patient findPatientByEmail(String email) {
        patientValidator.validateEmail(email);
        return patientRepository.findByUser_Email(email)
                .orElseThrow(() -> new PatientNotFoundException(email));
    }
}
