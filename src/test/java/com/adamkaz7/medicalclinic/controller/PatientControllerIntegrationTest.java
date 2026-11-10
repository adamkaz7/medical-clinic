package com.adamkaz7.medicalclinic.controller;

import com.adamkaz7.medicalclinic.command.ChangePatientPasswordCommand;
import com.adamkaz7.medicalclinic.command.UpdatePatientCommand;
import com.adamkaz7.medicalclinic.model.Patient;
import com.adamkaz7.medicalclinic.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.json.JsonMapper;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PatientControllerIntegrationTest {
    private static final String EMAIL = "test@test.com";
    private static final LocalDate BIRTHDAY = LocalDate.of(2000, 1, 1);

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JsonMapper jsonMapper;
    @Autowired
    private PatientRepository patientRepository;

    @BeforeEach
    public void setup() {
        patientRepository.deleteAll();
        patientRepository.save(new Patient(
                EMAIL,
                "test-password",
                "TEST08",
                "Jan",
                "Kowalski",
                "123456789",
                BIRTHDAY
        ));
    }

    @Test
    void updatePatientByEmail_IdCardNoChanged_ReturnsBadRequest() throws Exception {
        // given
        UpdatePatientCommand command = new UpdatePatientCommand(
                EMAIL,
                "password123",
                "TEST09",
                "Adam",
                "Testowy",
                "111222333",
                BIRTHDAY
        );
        // when & then
        mockMvc.perform(post("/patients/{email}", EMAIL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(jsonMapper.writeValueAsString(command)))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON),
                        jsonPath("$.status").value(400),
                        jsonPath("$.detail").value(
                                "Id card number cannot be changed when updating a patient"
                        ));
        Patient savedPatient = patientRepository.findByEmail(EMAIL).orElseThrow();
        assertAll(
                () -> assertEquals("TEST08", savedPatient.getIdCardNo()),
                () -> assertEquals("123456789", savedPatient.getPhoneNumber())
        );
    }

    @Test
    void updatePatientByEmail_AnyFieldIsNull_ReturnsBadRequest() throws Exception {
        // given
        LocalDate newBirthday = LocalDate.of(1998, 10, 31);
        List<UpdatePatientCommand> commands = List.of(
                new UpdatePatientCommand(null, "test-password", "TEST08", "Jan",
                        "Kowalski", "111222333", newBirthday),
                new UpdatePatientCommand(EMAIL, null, "TEST08", "Jan",
                        "Kowalski", "111222333", newBirthday),
                new UpdatePatientCommand(EMAIL, "test-password", null, "Jan",
                        "Kowalski", "111222333", newBirthday),
                new UpdatePatientCommand(EMAIL, "test-password", "TEST08", null,
                        "Kowalski", "111222333", newBirthday),
                new UpdatePatientCommand(EMAIL, "test-password", "TEST08", "Jan",
                        null, "111222333", newBirthday),
                new UpdatePatientCommand(EMAIL, "test-password", "TEST08", "Jan",
                        "Kowalski", null, newBirthday),
                new UpdatePatientCommand(EMAIL, "test-password", "TEST08", "Jan",
                        "Kowalski", "111222333", null)
        );
        List<String> expectedMessages = List.of(
                "Email is required",
                "Password is required",
                "ID card number is required",
                "First name is required",
                "Last name is required",
                "Phone number is required",
                "Birthday is required"
        );
        // when & then
        for (int i = 0; i < commands.size(); i++) {
            UpdatePatientCommand command = commands.get(i);
            String expectedMessage = expectedMessages.get(i);
            mockMvc.perform(post("/patients/{email}", EMAIL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonMapper.writeValueAsString(command)))
                    .andExpectAll(
                            status().isBadRequest(),
                            content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON),
                            jsonPath("$.status").value(400),
                            jsonPath("$.detail").value(expectedMessage));
        }
        Patient savedPatient = patientRepository.findByEmail(EMAIL).orElseThrow();
        assertAll(
                () -> assertEquals(EMAIL, savedPatient.getEmail()),
                () -> assertEquals("test-password", savedPatient.getPassword()),
                () -> assertEquals("TEST08", savedPatient.getIdCardNo()),
                () -> assertEquals("Jan",  savedPatient.getFirstName()),
                () -> assertEquals("Kowalski", savedPatient.getLastName()),
                () -> assertEquals("123456789", savedPatient.getPhoneNumber()),
                () -> assertEquals(BIRTHDAY, savedPatient.getBirthday())
        );
    }

    @Test
    void changePatientPassword_PasswordIsNull_ReturnsBadRequest() throws Exception {
        // given
        ChangePatientPasswordCommand command = new ChangePatientPasswordCommand(null);
        // when & then
        mockMvc.perform(patch("/patients/{email}/password", EMAIL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(command)))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON),
                        jsonPath("$.status").value(400),
                        jsonPath("$.detail").value("Password is required")
                );
        Patient savedPatient = patientRepository.findByEmail(EMAIL).orElseThrow();
        assertEquals("test-password", savedPatient.getPassword());
    }

    @Test
    void updatePatientByEmail_ValidCommand_ReturnsOk() throws Exception {
        // given
        LocalDate newBirthday = LocalDate.of(1998, 10, 31);
        UpdatePatientCommand command = new UpdatePatientCommand(
                EMAIL,
                "new-password",
                "TEST08",
                "Adam",
                "Testowy",
                "555666777",
                newBirthday
        );
        // when & then
        mockMvc.perform(post("/patients/{email}", EMAIL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(command)))
                .andExpectAll(
                        status().isOk(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                        jsonPath("$.email").value(EMAIL),
                        jsonPath("$.idCardNo").value("TEST08"),
                        jsonPath("$.firstName").value("Adam"),
                        jsonPath("$.lastName").value("Testowy"),
                        jsonPath("$.phoneNumber").value("555666777"),
                        jsonPath("$.birthday").value(newBirthday.toString()),
                        jsonPath("$.password").doesNotExist()
                );
        Patient savedPatient = patientRepository.findByEmail(EMAIL).orElseThrow();
        assertAll(
                () -> assertEquals(EMAIL, savedPatient.getEmail()),
                () -> assertEquals("TEST08", savedPatient.getIdCardNo()),
                () -> assertEquals("Adam", savedPatient.getFirstName()),
                () -> assertEquals("Testowy", savedPatient.getLastName()),
                () -> assertEquals("555666777", savedPatient.getPhoneNumber()),
                () -> assertEquals("new-password", savedPatient.getPassword()),
                () -> assertEquals(newBirthday, savedPatient.getBirthday())
        );
    }
}
