package com.adamkaz7.medicalclinic.controller;

import com.adamkaz7.medicalclinic.dto.CreatePatientCommand;
import com.adamkaz7.medicalclinic.dto.PatientResponse;
import com.adamkaz7.medicalclinic.model.Patient;
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
    public List<PatientResponse> getAllPatients() {
        return patientService.getAllPatients()
                .stream()
                .map(PatientResponse::from)
                .toList();
    }

    @GetMapping("/{email}")
    public PatientResponse getPatientByEmail(@PathVariable("email") String email) {
        Patient patient = patientService.getPatientByEmail(email);
        return PatientResponse.from(patient);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PatientResponse addPatient(@RequestBody CreatePatientCommand command) {
        Patient patient = patientService.addPatient(command.toPatient());
        return PatientResponse.from(patient);
    }

    @DeleteMapping("/{email}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePatient(@PathVariable("email") String email) {
        patientService.deletePatientByEmail(email);
    }

    @PostMapping("/{email}")
    public PatientResponse updatePatientByEmail(
            @PathVariable("email") String email,
            @RequestBody CreatePatientCommand command
    ) {
        Patient patient = patientService.updatePatientByEmail(email, command.toPatient());
        return PatientResponse.from(patient);
    }
}
