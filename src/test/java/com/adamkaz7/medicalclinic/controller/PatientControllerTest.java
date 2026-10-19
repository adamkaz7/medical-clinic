package com.adamkaz7.medicalclinic.controller;

import com.adamkaz7.medicalclinic.command.ChangePatientPasswordCommand;
import com.adamkaz7.medicalclinic.command.CreatePatientCommand;
import com.adamkaz7.medicalclinic.command.UpdatePatientCommand;
import com.adamkaz7.medicalclinic.dto.PatientDto;
import com.adamkaz7.medicalclinic.exception.PatientAlreadyExistsException;
import com.adamkaz7.medicalclinic.exception.PatientNotFoundException;
import com.adamkaz7.medicalclinic.exception.PatientValidationException;
import com.adamkaz7.medicalclinic.service.PatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import tools.jackson.databind.json.JsonMapper;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PatientController.class)
public class PatientControllerTest {
    private static final String EMAIL = "john@example.com";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JsonMapper jsonMapper;
    @MockitoBean
    private PatientService patientService;

    private PatientDto patientDto;
    private PatientDto secondPatientDto;

    @BeforeEach
    void setUp() {
        patientDto = new PatientDto(
                "john@example.com",
                "TEST08",
                "John",
                "Doe",
                "123456789",
                LocalDate.of(2000, 1, 10)
        );
        secondPatientDto = new PatientDto(
                "jane@example.com",
                "TEST09",
                "Jane",
                "Smith",
                "987654321",
                LocalDate.of(1995, 5, 20)
        );
    }

    @Test
    void getAllPatients_PatientsExist_ReturnOk() throws Exception {
        // given
        when(patientService.getAllPatients()).thenReturn(List.of(patientDto, secondPatientDto));
        // when
        ResultActions result = mockMvc.perform(get("/patients"));
        // then
        result.andExpectAll(
                status().isOk(),
                content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                jsonPath("$").isArray(),
                jsonPath("$.length()").value(2),
                jsonPath("$[0].email").value("john@example.com"),
                jsonPath("$[0].idCardNo").value("TEST08"),
                jsonPath("$[0].firstName").value("John"),
                jsonPath("$[0].lastName").value("Doe"),
                jsonPath("$[0].phoneNumber").value("123456789"),
                jsonPath("$[0].birthday").value("2000-01-10"),
                jsonPath("$[1].email").value("jane@example.com"),
                jsonPath("$[0].password").doesNotExist(),
                jsonPath("$[1].password").doesNotExist()
        );
        verify(patientService).getAllPatients();
    }

    @Test
    void getAllPatients_NoPatientsExist_ReturnEmptyList() throws Exception {
        // given
        when(patientService.getAllPatients()).thenReturn(List.of());
        // when
        ResultActions result = mockMvc.perform(get("/patients"));
        // then
        result.andExpectAll(
                status().isOk(),
                content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                jsonPath("$").isArray(),
                jsonPath("$").isEmpty()
        );
        verify(patientService).getAllPatients();
    }

    @Test
    void getPatientByEmail_PatientExist_ReturnOk() throws Exception {
        // given
        when(patientService.getPatientByEmail(EMAIL)).thenReturn(patientDto);
        // when
        ResultActions result = mockMvc.perform(get("/patients/{email}", EMAIL));
        // then
        result.andExpectAll(
                status().isOk(),
                content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                jsonPath("$.email").value(EMAIL),
                jsonPath("$.idCardNo").value("TEST08"),
                jsonPath("$.firstName").value("John"),
                jsonPath("$.lastName").value("Doe"),
                jsonPath("$.phoneNumber").value("123456789"),
                jsonPath("$.birthday").value("2000-01-10"),
                jsonPath("$.password").doesNotExist()
        );
        verify(patientService).getPatientByEmail(EMAIL);
    }

    @Test
    void addPatient_ValidCommand_ReturnCreated() throws Exception {
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
        when(patientService.addPatient(command)).thenReturn(patientDto);
        // when
        ResultActions result = mockMvc.perform(post("/patients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(command))
        );
        // then
        result.andExpectAll(
                status().isCreated(),
                content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                jsonPath("$.email").value(EMAIL),
                jsonPath("$.idCardNo").value("TEST08"),
                jsonPath("$.firstName").value("John"),
                jsonPath("$.lastName").value("Doe"),
                jsonPath("$.phoneNumber").value("123456789"),
                jsonPath("$.birthday").value("2000-01-10"),
                jsonPath("$.password").doesNotExist()
        );
        verify(patientService).addPatient(command);
    }

    @Test
    void updatePatientByEmail_validCommand_ReturnsOk() throws Exception {
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
        when(patientService.updatePatientByEmail(EMAIL, command)).thenReturn(updatedDto);
        // when
        ResultActions result = mockMvc.perform(post("/patients/{email}", EMAIL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(command))
        );
        // then
        result.andExpectAll(
                status().isOk(),
                content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                jsonPath("$.email").value(EMAIL),
                jsonPath("$.idCardNo").value("TEST08"),
                jsonPath("$.firstName").value("John"),
                jsonPath("$.lastName").value("Doe"),
                jsonPath("$.phoneNumber").value("555666777"),
                jsonPath("$.birthday").value("2000-01-10"),
                jsonPath("$.password").doesNotExist()
        );
        verify(patientService).updatePatientByEmail(EMAIL, command);
    }

    @Test
    void deletePatient_PatientExist_ReturnsNoContent() throws Exception {
        // when
        ResultActions result = mockMvc.perform(
                delete("/patients/{email}", EMAIL)
        );
        // then
        result.andExpectAll(
                status().isNoContent(),
                content().string("")
        );
        verify(patientService).deletePatientByEmail(EMAIL);
    }

    @Test
    void changePatientPassword_ValidCommand_ReturnsNoContent() throws Exception {
        // given
        ChangePatientPasswordCommand command = new ChangePatientPasswordCommand("new-password");
        // when
        ResultActions result = mockMvc.perform(
                patch("/patients/{email}/password", EMAIL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(command))
        );
        // then
        result.andExpectAll(
                status().isNoContent(),
                content().string("")
        );
        verify(patientService).changePatientPassword(EMAIL, command.password());
    }

    @Test
    void getPatientByEmail_PatientDoesNotExist_RetunNotFound() throws Exception {
        // given
        when(patientService.getPatientByEmail(EMAIL)).thenThrow(new PatientNotFoundException(EMAIL));
        // when & then
        mockMvc.perform(get("/patients/{email}", EMAIL))
                .andExpectAll(status().isNotFound(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON),
                        jsonPath("$.status").value(404),
                        jsonPath("$.detail").value(
                                "Patient with email " + EMAIL + " was not found"
                        )
                );
        verify(patientService).getPatientByEmail(EMAIL);
    }

    @Test
    void addPatient_EmailAlreadyExists_ReturnsConflict() throws Exception {
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
        when(patientService.addPatient(command)).thenThrow(new PatientAlreadyExistsException(EMAIL));
        // when & then
        mockMvc.perform(
                post("/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(command))
        ).andExpectAll(
                status().isConflict(),
                content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON),
                jsonPath("$.status").value(409),
                jsonPath("$.detail").value("Patient with email " + EMAIL + " already exists")
        );
        verify(patientService).addPatient(command);
    }

    @Test
    void updatePatientByEmail_PatientDoesNotExist_ReturnsNotFound() throws Exception {
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
        when(patientService.updatePatientByEmail(EMAIL, command)).thenThrow(new PatientNotFoundException(EMAIL));
        // when & then
        mockMvc.perform(
                post("/patients/{email}", EMAIL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(command))
        ).andExpectAll(
                status().isNotFound(),
                content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON),
                jsonPath("$.status").value(404),
                jsonPath("$.detail").value("Patient with email " + EMAIL + " was not found")
        );
        verify(patientService).updatePatientByEmail(EMAIL, command);
    }

    @Test
    void deletePatient_PatientDoesNotExist_ReturnsNotFound() throws Exception {
        // given
        doThrow(new PatientNotFoundException(EMAIL)).when(patientService).deletePatientByEmail(EMAIL);
        // when & then
        mockMvc.perform(delete("/patients/{email}", EMAIL))
                .andExpectAll(
                        status().isNotFound(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON),
                        jsonPath("$.status").value(404),
                        jsonPath("$.detail").value(
                                "Patient with email " + EMAIL + " was not found"
                        )
                );
        verify(patientService).deletePatientByEmail(EMAIL);
    }

    @Test
    void changePatientPassword_PatientDoesNotExist_ReturnsNotFound() throws Exception {
        // given
        ChangePatientPasswordCommand command = new ChangePatientPasswordCommand("new-password");
        doThrow(new PatientNotFoundException(EMAIL))
                .when(patientService)
                .changePatientPassword(EMAIL, command.password());
        // when & then
        mockMvc.perform(
                patch("/patients/{email}/password", EMAIL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(command))
        ).andExpectAll(
                status().isNotFound(),
                content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON),
                jsonPath("$.status").value(404),
                jsonPath("$.detail").value("Patient with email " + EMAIL + " was not found")
        );
        verify(patientService).changePatientPassword(EMAIL, command.password());
    }

    @Test
    void addPatient_ValidationFails_ReturnsBadRequest() throws Exception {
        // given
        CreatePatientCommand command = new CreatePatientCommand(
                "",
                "test-password",
                "TEST08",
                "John",
                "Doe",
                "123456789",
                LocalDate.of(2000, 1, 10)
        );
        when(patientService.addPatient(command)).thenThrow(new PatientValidationException("Email is required"));
        // when & then
        mockMvc.perform(
                post("/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(command))
        ).andExpectAll(
                status().isBadRequest(),
                content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON),
                jsonPath("$.status").value(400),
                jsonPath("$.detail").value("Email is required")
        );
        verify(patientService).addPatient(command);
    }

    @Test
    void getAllPatients_UnexpectedError_ReturnsInternalServerError() throws Exception {
        // given
        when(patientService.getAllPatients()).thenThrow(new RuntimeException("Database connection failed"));
        // when & then
        mockMvc.perform(get("/patients"))
                .andExpectAll(
                        status().isInternalServerError(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON),
                        jsonPath("$.status").value(500),
                        jsonPath("$.detail").value("Unknown error")
                );
        verify(patientService).getAllPatients();
    }
}
