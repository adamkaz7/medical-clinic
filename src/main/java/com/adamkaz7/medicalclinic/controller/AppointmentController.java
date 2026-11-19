package com.adamkaz7.medicalclinic.controller;

import com.adamkaz7.medicalclinic.command.CreateAppointmentCommand;
import com.adamkaz7.medicalclinic.dto.AppointmentDto;
import com.adamkaz7.medicalclinic.service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Appointments", description = "Appointment management")
@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
@Slf4j
public class AppointmentController {
    private final AppointmentService appointmentService;

    @Operation(summary = "Create appointment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Appointment created"),
            @ApiResponse(responseCode = "409", description = "Doctor already has an appointment in this time range",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AppointmentDto addAppointment(@Valid @RequestBody CreateAppointmentCommand command) {
        log.info("Received POST /appointments request: doctorId={}", command.doctorId());
        return appointmentService.addAppointment(command);
    }

    @Operation(summary = "Get available appointments")
    @ApiResponse(responseCode = "200", description = "Available appointment list")
    @GetMapping
    public Page<AppointmentDto> getAllAppointments(Pageable pageable) {
        log.info("Received GET /appointments request: page={}, size={}", pageable.getPageNumber(), pageable.getPageSize());
        return appointmentService.getAllAppointments(pageable);
    }

    @Operation(summary = "Get an appointment by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Appointment found"),
            @ApiResponse(responseCode = "404", description = "Appointment not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    @GetMapping("/{appointmentId}")
    public AppointmentDto getAppointmentById(@PathVariable Long appointmentId) {
        log.info("Received GET /appointments/{} request", appointmentId);
        return appointmentService.getAppointmentById(appointmentId);
    }

    @Operation(summary = "Book appointment for a patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Appointment booked"),
            @ApiResponse(responseCode = "400", description = "Appointment is already booked or has already started",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PatchMapping("/{appointmentId}/patient/{patientId}")
    public void assignPatientToAppointment(@PathVariable Long appointmentId, @PathVariable Long patientId) {
        log.info("Received PATCH /appointments/{}/patient/{} request", appointmentId, patientId);
        appointmentService.assignPatientToAppointment(appointmentId, patientId);
    }

    @Operation(summary = "Delete appointment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Appointment deleted"),
            @ApiResponse(responseCode = "404", description = "Appointment not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    @DeleteMapping("/{appointmentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAppointment(@PathVariable Long appointmentId) {
        log.info("Received DELETE /appointments/{} request", appointmentId);
        appointmentService.deleteAppointment(appointmentId);
    }
}
