package com.adamkaz7.medicalclinic.controller;

import com.adamkaz7.medicalclinic.command.ChangeDoctorPasswordCommand;
import com.adamkaz7.medicalclinic.command.CreateDoctorCommand;
import com.adamkaz7.medicalclinic.command.UpdateDoctorCommand;
import com.adamkaz7.medicalclinic.dto.DoctorDto;
import com.adamkaz7.medicalclinic.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doctors")
@RequiredArgsConstructor
public class DoctorController {
    private final DoctorService doctorService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DoctorDto addDoctor(@RequestBody CreateDoctorCommand command) {
        return doctorService.addDoctor(command);
    }

    @GetMapping("/{doctorId}")
    public DoctorDto getDoctorById(@PathVariable Long doctorId) {
        return doctorService.getDoctorById(doctorId);
    }

    @GetMapping
    public List<DoctorDto> getAllDoctors() {
        return doctorService.getAllDoctors();
    }

    @PatchMapping("/{doctorId}/facilities/{facilityId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void assignDoctorToMedicalFacility(@PathVariable Long doctorId, @PathVariable Long facilityId) {
        doctorService.assignDoctorToMedicalFacility(doctorId, facilityId);
    }

    @PatchMapping("/{doctorId}/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changeDoctorPassword(@PathVariable Long doctorId, @RequestBody ChangeDoctorPasswordCommand command) {
        doctorService.changeDoctorPassword(doctorId, command.password());
    }

    @DeleteMapping("/{doctorId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDoctor(@PathVariable Long doctorId) {
        doctorService.deleteDoctor(doctorId);
    }

    @PostMapping("/{doctorId}")
    public DoctorDto updateDoctorById(@PathVariable Long doctorId, @RequestBody UpdateDoctorCommand command) {
        return doctorService.updateDoctorById(doctorId, command);
    }
}
