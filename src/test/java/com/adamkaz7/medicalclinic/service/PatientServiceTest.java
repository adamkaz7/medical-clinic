package com.adamkaz7.medicalclinic.service;

import com.adamkaz7.medicalclinic.command.CreatePatientCommand;
import com.adamkaz7.medicalclinic.command.UpdatePatientCommand;
import com.adamkaz7.medicalclinic.dto.PatientDto;
import com.adamkaz7.medicalclinic.dto.UserDto;
import com.adamkaz7.medicalclinic.exception.patient.PatientAlreadyExistsException;
import com.adamkaz7.medicalclinic.exception.patient.PatientNotFoundException;
import com.adamkaz7.medicalclinic.exception.patient.PatientValidationException;
import com.adamkaz7.medicalclinic.mapper.PatientMapper;
import com.adamkaz7.medicalclinic.model.Patient;
import com.adamkaz7.medicalclinic.model.User;
import com.adamkaz7.medicalclinic.repository.PatientRepository;
import com.adamkaz7.medicalclinic.repository.UserRepository;
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
    private static final Long USER_ID = 1L;

    private PatientRepository patientRepository;
    private UserRepository userRepository;
    private PatientMapper patientMapper;
    private PatientValidator patientValidator;
    private PatientService patientService;
    private Patient patient;
    private PatientDto expectedDto;
    private User user;
    private UserDto userDto;

    @BeforeEach
    public void setup() {
        patientRepository = mock(PatientRepository.class);
        patientMapper = mock(PatientMapper.class);
        patientValidator = mock(PatientValidator.class);
        userRepository = mock(UserRepository.class);
        patientService = new PatientService(patientRepository, patientMapper, patientValidator, userRepository);
        user = new User(
                USER_ID,
                EMAIL,
                "test-password",
                "John",
                "Doe"
        );
        userDto = new UserDto(USER_ID, EMAIL, "John", "Doe");
        patient = Patient.builder()
                .idCardNo("TEST08")
                .phoneNumber("123456789")
                .birthday(LocalDate.of(2000, 1, 10))
                .user(user)
                .build();
        expectedDto = new PatientDto(
                "TEST08",
                "123456789",
                LocalDate.of(2000, 1, 10),
                userDto
        );
    }

    @Test
    void addPatient_ValidCommand_ReturnsPatientDto() {
        // given
        CreatePatientCommand command = new CreatePatientCommand(
                USER_ID,
                "TEST08",
                "123456789",
                LocalDate.of(2000, 1, 10)
        );
        when(patientRepository.save(patient)).thenReturn(patient);
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
        when(patientMapper.toEntity(command)).thenReturn(patient);
        when(patientMapper.toDto(patient)).thenReturn(expectedDto);
        //when
        PatientDto result = patientService.addPatient(command);
        //then
        assertAll(
                () -> assertEquals(expectedDto, result),
                () -> verify(patientRepository).save(patient)
        );
    }

    @Test
    void getPatientByEmail_PatientExists_ReturnsPatientDto() {
        // given
        when(patientRepository.findByUser_Email(EMAIL)).thenReturn(Optional.of(patient));
        when(patientMapper.toDto(patient)).thenReturn(expectedDto);
        // when
        PatientDto result = patientService.getPatientByEmail(EMAIL);
        // then
        assertAll(
                () -> assertEquals(expectedDto, result),
                () -> verify(patientValidator).validateEmail(EMAIL)
        );
    }

    @Test
    void getAllPatients_PatientExists_ReturnsPatientDto() {
        // given
        User secondUser = new User(
                2L,
                "jane@example.com",
                "another-password",
                "Jane",
                "Smith"
        );
        Patient secondPatient = Patient.builder()
                .idCardNo("TEST09")
                .phoneNumber("987654321")
                .birthday(LocalDate.of(1995, 5, 20))
                .user(secondUser)
                .build();
        PatientDto secondDto = new PatientDto(
                "TEST09",
                "987654321",
                LocalDate.of(1995, 5, 20),
                new UserDto(2L, "jane@example.com", "Jane", "Smith")
        );
        when(patientRepository.findAll()).thenReturn(List.of(patient, secondPatient));
        when(patientMapper.toDto(patient)).thenReturn(expectedDto);
        when(patientMapper.toDto(secondPatient)).thenReturn(secondDto);
        // when
        List<PatientDto> result = patientService.getAllPatients();
        // then
        assertAll(
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
                () -> assertNotNull(result),
                () -> assertTrue(result.isEmpty())
        );
    }

    @Test
    void updatePatientByEmail_PatientExists_ReturnsUpdatedPatientDto() {
        // given
        patientService = new PatientService(patientRepository, patientMapper, new PatientValidator(), userRepository);
        UpdatePatientCommand command = new UpdatePatientCommand(
                "TEST08",
                "555666777",
                LocalDate.of(2000, 1, 10)
        );
        PatientDto updatedDto = new PatientDto(
                "TEST08",
                "555666777",
                LocalDate.of(2000, 1, 10),
                userDto
        );
        when(patientRepository.save(patient)).thenReturn(patient);
        when(patientRepository.findByUser_Email(EMAIL)).thenReturn(Optional.of(patient));
        when(patientMapper.toDto(patient)).thenReturn(updatedDto);
        // when
        PatientDto result = patientService.updatePatientByEmail(EMAIL, command);
        // then
        assertAll(
                () -> assertEquals(updatedDto, result),
                () -> assertEquals("555666777", patient.getPhoneNumber()),
                () -> assertEquals(EMAIL, patient.getUser().getEmail()),
                () -> assertEquals("TEST08", patient.getIdCardNo()),
                () -> verify(patientRepository).save(patient)
        );
    }

    @Test
    void deletePatientByEmail_PatientExists_DeletePatient() {
        // given
        when(patientRepository.findByUser_Email(EMAIL)).thenReturn(Optional.of(patient));
        // when
        patientService.deletePatientByEmail(EMAIL);
        // then
        assertAll(
                () -> verify(patientValidator).validateEmail(EMAIL),
                () -> verify(patientRepository).delete(patient)
        );
    }

    @Test
    void getPatientByEmail_PatientDoesNotExist_ThrowsPatientNotFoundException() {
        // given
        when(patientRepository.findByUser_Email(EMAIL)).thenReturn(Optional.empty());
        // when & then
        PatientNotFoundException exception = assertThrows(PatientNotFoundException.class,
                () -> patientService.getPatientByEmail(EMAIL));
        assertAll(
                () -> assertEquals("Patient with email " + EMAIL + " was not found", exception.getMessage()),
                () -> assertEquals(HttpStatus.NOT_FOUND, exception.getStatus())
        );
    }

    @Test
    void addPatient_IdCardNoAlreadyExists_ThrowsPatientAlreadyExistsException() {
        // given
        CreatePatientCommand command = new CreatePatientCommand(
                USER_ID,
                "TEST08",
                "123456789",
                LocalDate.of(2000, 1, 10)
        );
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
        when(patientRepository.existsByIdCardNo(command.idCardNo())).thenReturn(true);
        // when & then
        PatientAlreadyExistsException exception = assertThrows(
                PatientAlreadyExistsException.class,
                () -> patientService.addPatient(command));
        assertAll(
                () -> assertEquals("Patient with id card number TEST08 already exists", exception.getMessage()),
                () -> assertEquals(HttpStatus.CONFLICT, exception.getStatus())
        );
    }

    @Test
    void updatePatientByEmail_PatientDoesNotExist_ThrowsPatientNotFoundException() {
        // given
        UpdatePatientCommand command = new UpdatePatientCommand(
                "TEST08",
                "123456789",
                LocalDate.of(2000, 1, 10)
        );
        when(patientRepository.findByUser_Email(EMAIL)).thenReturn(Optional.empty());
        // when & then
        PatientNotFoundException exception = assertThrows(PatientNotFoundException.class,
                () -> patientService.updatePatientByEmail(EMAIL, command));
        assertAll(
                () -> assertEquals("Patient with email " + EMAIL + " was not found", exception.getMessage()),
                () -> assertEquals(HttpStatus.NOT_FOUND, exception.getStatus())
        );
    }

    @Test
    void deletePatientByEmail_PatientDoesNotExist_ThrowsPatientNotFoundException() {
        // given
        when(patientRepository.findByUser_Email(EMAIL)).thenReturn(Optional.empty());
        // when & then
        PatientNotFoundException exception = assertThrows(PatientNotFoundException.class,
                () -> patientService.deletePatientByEmail(EMAIL));
        assertAll(
                () -> assertEquals("Patient with email " + EMAIL + " was not found", exception.getMessage()),
                () -> assertEquals(HttpStatus.NOT_FOUND, exception.getStatus())
        );
    }

    @Test
    void updatePatientByEmail_IdCardNoChanged_ThrowsPatientValidationException() {
        // given
        patientService = new PatientService(patientRepository, patientMapper, new PatientValidator(), userRepository);
        UpdatePatientCommand command = new UpdatePatientCommand(
                "TEST09",
                "111222333",
                LocalDate.of(2000, 1, 10)
        );
        when(patientRepository.findByUser_Email(EMAIL)).thenReturn(Optional.of(patient));
        // when & then
        PatientValidationException exception = assertThrows(
                PatientValidationException.class,
                () -> patientService.updatePatientByEmail(EMAIL, command)
        );
        assertAll(
                () -> assertEquals("Id card number cannot be changed when updating a patient",
                        exception.getMessage()),
                () -> assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus()),
                () -> assertEquals("TEST08", patient.getIdCardNo()),
                () -> assertEquals("123456789", patient.getPhoneNumber()),
                () -> verify(patientRepository, never()).save(any(Patient.class))
        );
    }
}
