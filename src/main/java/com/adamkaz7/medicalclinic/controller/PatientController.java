package com.adamkaz7.medicalclinic.controller;

import com.adamkaz7.medicalclinic.command.ChangePatientPasswordCommand;
import com.adamkaz7.medicalclinic.command.CreatePatientCommand;
import com.adamkaz7.medicalclinic.dto.PatientDto;
import com.adamkaz7.medicalclinic.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
public class PatientController {
    private final PatientService patientService;

    @GetMapping
    public List<PatientDto> getAllPatients() {
        return patientService.getAllPatients();
    }

    @GetMapping("/{email}")
    public PatientDto getPatientByEmail(@PathVariable String email) {
        return patientService.getPatientByEmail(email);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PatientDto addPatient(@RequestBody CreatePatientCommand command) {
        return patientService.addPatient(command);
    }

    @DeleteMapping("/{email}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePatient(@PathVariable("email") String email) {
        patientService.deletePatientByEmail(email);
    }

    @PostMapping("/{email}")
    public PatientDto updatePatientByEmail(
            @PathVariable String email,
            @RequestBody CreatePatientCommand command
    ) {
        return patientService.updatePatientByEmail(email, command);
    }

    @PatchMapping("/{email}/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePatientPassword(
            @PathVariable String email,
            @RequestBody ChangePatientPasswordCommand command
    ) {
        patientService.changePatientPassword(email, command.password());
    }
}