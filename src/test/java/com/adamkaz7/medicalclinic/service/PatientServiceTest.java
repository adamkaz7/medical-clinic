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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PatientServiceTest {
    private static final String EMAIL = "john@example.com";

    private PatientRepository patientRepository;
    private PatientMapper patientMapper;
    private PatientValidator patientValidator;
    private PatientService patientService;
    private Patient patient;
    private PatientDto expectedDto;

    @BeforeEach
    public void setup() {
        patientRepository = mock(PatientRepository.class);
        patientMapper = mock(PatientMapper.class);
        patientValidator = mock(PatientValidator.class);
        patientService = new PatientService(patientRepository, patientMapper, patientValidator);
        patient = new Patient(
                EMAIL,
                "test-password",
                "TEST08",
                "John",
                "Doe",
                "123456789",
                LocalDate.of(2000, 1, 10)
        );
        expectedDto = new PatientDto(
                EMAIL,
                "TEST08",
                "John",
                "Doe",
                "123456789",
                LocalDate.of(2000, 1, 10)
        );
    }

    @Test
    void addPatient_EmailIsAvailable_ReturnsPatientDto() {
        // given
        CreatePatientCommand command = new CreatePatientCommand(
                "john@example.com",
                "test-password",
                "TEST08",
                "John",
                "Doe",
                "123456789",
                LocalDate.of(2000, 1, 10)
        );
        when(patientMapper.toEntity(command)).thenReturn(patient);
        when(patientRepository.save(any(Patient.class))).thenReturn(patient);
        when(patientMapper.toDto(patient)).thenReturn(expectedDto);
        //when
        PatientDto result = patientService.addPatient(command);
        //then
        assertAll(
                "Create patient",
                () -> assertEquals(expectedDto, result),
                () -> verify(patientRepository).save(patient),
                () -> verify(patientValidator).validateCreatePatientCommand(command)
        );
    }

    @Test
    void getPatientByEmail_PatientExists_ReturnsPatientDto() {
        // given
        when(patientRepository.findByEmail(EMAIL)).thenReturn(Optional.of(patient));
        when(patientMapper.toDto(patient)).thenReturn(expectedDto);
        // when
        PatientDto result = patientService.getPatientByEmail(EMAIL);
        // then
        assertAll(
                "Retrieved patient",
                () -> assertEquals(expectedDto, result),
                () -> verify(patientValidator).validateEmail(EMAIL)
        );
    }

    @Test
    void getAllPatients_PatientExists_ReturnsPatientDto() {
        // given
        Patient secondPatient = new Patient(
                "jane@example.com",
                "another-password",
                "TEST09",
                "Jane",
                "Smith",
                "987654321",
                LocalDate.of(1995, 5, 20)
        );
        PatientDto secondDto = new PatientDto(
                "jane@example.com",
                "TEST09",
                "Jane",
                "Smith",
                "987654321",
                LocalDate.of(1995, 5, 20)
        );
        when(patientRepository.findAll()).thenReturn(List.of(patient, secondPatient));
        when(patientMapper.toDto(patient)).thenReturn(expectedDto);
        when(patientMapper.toDto(secondPatient)).thenReturn(secondDto);
        // when
        List<PatientDto> result = patientService.getAllPatients();
        // then
        assertAll(
                "Retrieved patients",
                () -> assertEquals(2, result.size()),
                () -> assertTrue(result.contains(expectedDto)),
                () -> assertTrue(result.contains(secondDto))
        );
    }

    @Test
    void getAllPatients_NoPatientsExists_ReturnsEmptyList() {
        //given
        when(patientRepository.findAll()).thenReturn(List.of());
        // when
        List<PatientDto> result = patientService.getAllPatients();
        // then
        assertAll(
                "Empty patient list",
                () -> assertNotNull(result),
                () -> assertTrue(result.isEmpty())
        );
    }

    @Test
    void updatePatientByEmail_PatientExists_ReturnsUpdatedPatientDto() {
        // given
        UpdatePatientCommand command = new UpdatePatientCommand(
                EMAIL,
                "test-password",
                "TEST08",
                "John",
                "Doe",
                "555666777",
                LocalDate.of(2000, 1, 10)
        );
        PatientDto updatedDto = new PatientDto(
                EMAIL,
                "TEST08",
                "John",
                "Doe",
                "555666777",
                LocalDate.of(2000, 1, 10)
        );
        when(patientRepository.findByEmail(EMAIL)).thenReturn(Optional.of(patient));
        when(patientRepository.save(patient)).thenReturn(patient);
        when(patientMapper.toDto(patient)).thenReturn(updatedDto);
        // when
        PatientDto result = patientService.updatePatientByEmail(EMAIL, command);
        // then
        assertAll(
                "Updated patient",
                () -> assertEquals(updatedDto, result),
                () -> verify(patientRepository).save(patient),
                () -> verify(patientValidator).validateUpdatePatientCommand(EMAIL, command)
        );
    }

    @Test
    void deletePatientByEmail_PatientExists_DeletePatient() {
        // given
        when(patientRepository.findByEmail(EMAIL)).thenReturn(Optional.of(patient));
        // when
        patientService.deletePatientByEmail(EMAIL);
        // then
        assertAll(
                "Delete patient",
                () -> verify(patientValidator).validateEmail(EMAIL),
                () -> verify(patientRepository).delete(patient)
        );
    }

    @Test
    void changePatientPassword_PatientExists_UpdatesPassword() {
        // given
        String newPassword = "new-password";
        when(patientRepository.findByEmail(EMAIL)).thenReturn(Optional.of(patient));
        // when
        patientService.changePatientPassword(EMAIL, newPassword);
        // then
        assertAll(
                "Changed patient password",
                () -> assertEquals(newPassword, patient.getPassword()),
                () -> verify(patientRepository).save(patient),
                () -> verify(patientValidator).validateEmail(EMAIL)
        );
    }

    @Test
    void getPatientByEmail_PatientDoesNotExist_ThrowsPatientNotFoundException() {
        // given
        when(patientRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());
        // when & then
        PatientNotFoundException exception = assertThrows(PatientNotFoundException.class,
                () -> patientService.getPatientByEmail(EMAIL));
        assertAll(
                "Patient not found",
                () -> assertEquals("Patient with email " + EMAIL + " was not found", exception.getMessage()),
                () -> assertEquals(HttpStatus.NOT_FOUND, exception.getStatus())
        );
    }

    @Test
    void addPatient_EmailAlreadyExists_ThrowsPatientAlreadyExistsException() {
        // given
        CreatePatientCommand command = new CreatePatientCommand(
                EMAIL,
                "test-password",
                "TEST08",
                "John",
                "Doe",
                "123456789",
                LocalDate.of(2000, 1, 10)
        );
        when(patientMapper.toEntity(command)).thenReturn(patient);
    when(patientRepository.existsByEmail(EMAIL)).thenReturn(true);
        // when & then
        PatientAlreadyExistsException exception = assertThrows(PatientAlreadyExistsException.class,
                () -> patientService.addPatient(command));
        assertAll(
                "Patient already exists",
                () -> assertEquals("Patient with email " + EMAIL + " already exists", exception.getMessage()),
                () -> assertEquals(HttpStatus.CONFLICT, exception.getStatus())
        );
    }

    @Test
    void updatePatientByEmail_PatientDoesNotExist_ThrowsPatientNotFoundException() {
        // given
        UpdatePatientCommand command = new UpdatePatientCommand(
                EMAIL,
                "test-password",
                "TEST08",
                "John",
                "Doe",
                "123456789",
                LocalDate.of(2000, 1, 10)
        );
        when(patientMapper.toEntity(command)).thenReturn(patient);
        when(patientRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());
        // when & then
        PatientNotFoundException exception = assertThrows(PatientNotFoundException.class,
                () -> patientService.updatePatientByEmail(EMAIL, command));
        assertAll(
                "Patient to update not found",
                () -> assertEquals("Patient with email " + EMAIL + " was not found", exception.getMessage()),
                () -> assertEquals(HttpStatus.NOT_FOUND, exception.getStatus())
        );
    }

    @Test
    void deletePatientByEmail_PatientDoesNotExist_ThrowsPatientNotFoundException() {
        // given
        when(patientRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());
        // when & then
        PatientNotFoundException exception = assertThrows(PatientNotFoundException.class,
                () -> patientService.deletePatientByEmail(EMAIL));
        assertAll(
                "Patient to delete not found",
                () -> assertEquals("Patient with email " + EMAIL + " was not found", exception.getMessage()),
                () -> assertEquals(HttpStatus.NOT_FOUND, exception.getStatus())
        );
    }

    @Test
    void changePatientPassword_PatientDoesNotExist_ThrowsPatientNotFoundException() {
        // given
        String newPassword = "new-password";
        when(patientRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());
        // when & then
        PatientNotFoundException exception = assertThrows(PatientNotFoundException.class,
                () -> patientService.changePatientPassword(EMAIL, newPassword));
        assertAll(
                "Patient for password change not found",
                () -> assertEquals("Patient with email " + EMAIL + " was not found", exception.getMessage()),
                () -> assertEquals(HttpStatus.NOT_FOUND, exception.getStatus()),
                () -> verify(patientRepository, never()).save(any(Patient.class))
        );
    }
}
