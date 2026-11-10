package com.adamkaz7.medicalclinic.controller;

import com.adamkaz7.medicalclinic.command.CreateAppointmentCommand;
import com.adamkaz7.medicalclinic.dto.AppointmentDto;
import com.adamkaz7.medicalclinic.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
public class AppointmentController {
    private final AppointmentService appointmentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AppointmentDto addAppointment(@Valid @RequestBody CreateAppointmentCommand command) {
        return appointmentService.addAppointment(command);
    }

    @GetMapping
    public List<AppointmentDto> getAllAppointments() {
        return appointmentService.getAllAppointments();
    }

    @GetMapping("/{appointmentId}")
    public AppointmentDto getAppointmentById(@PathVariable Long appointmentId) {
        return appointmentService.getAppointmentById(appointmentId);
    }

    @PatchMapping("/{appointmentId}/patient/{patientId}")
    public void assignPatientToAppointment(@PathVariable Long appointmentId, @PathVariable Long patientId) {
        appointmentService.assignPatientToAppointment(appointmentId, patientId);
    }

    @DeleteMapping("/{appointmentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAppointment(@PathVariable Long appointmentId) {
        appointmentService.deleteAppointment(appointmentId);
    }
}
