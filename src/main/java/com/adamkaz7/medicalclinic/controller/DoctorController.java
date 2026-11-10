package com.adamkaz7.medicalclinic.controller;

import com.adamkaz7.medicalclinic.command.ChangeDoctorPasswordCommand;
import com.adamkaz7.medicalclinic.command.CreateDoctorCommand;
import com.adamkaz7.medicalclinic.command.UpdateDoctorCommand;
import com.adamkaz7.medicalclinic.dto.DoctorDto;
import com.adamkaz7.medicalclinic.service.DoctorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Doctors", description = "Doctors management")
@RestController
@RequestMapping("/doctors")
@RequiredArgsConstructor
public class DoctorController {
    private final DoctorService doctorService;

    @Operation(summary = "Create a doctor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Doctor created"),
            @ApiResponse(responseCode = "409", description = "Doctor with the provided email already exists",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DoctorDto addDoctor(@RequestBody CreateDoctorCommand command) {
        return doctorService.addDoctor(command);
    }

    @Operation(summary = "Get doctor by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Doctor found"),
            @ApiResponse(responseCode = "404", description = "Doctor not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    @GetMapping("/{doctorId}")
    public DoctorDto getDoctorById(@PathVariable Long doctorId) {
        return doctorService.getDoctorById(doctorId);
    }

    @Operation(summary = "Get all doctors")
    @ApiResponse(responseCode = "200", description = "Doctor list returned")
    @GetMapping
    public List<DoctorDto> getAllDoctors() {
        return doctorService.getAllDoctors();
    }

    @Operation(summary = "Assign doctor to a medical facilityD")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Doctor assigned to the medical facility"),
            @ApiResponse(responseCode = "404", description = "Doctor or medical facility not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PatchMapping("/{doctorId}/facilities/{facilityId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void assignDoctorToMedicalFacility(@PathVariable Long doctorId, @PathVariable Long facilityId) {
        doctorService.assignDoctorToMedicalFacility(doctorId, facilityId);
    }

    @Operation(summary = "Change doctor password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Password changed"),
            @ApiResponse(responseCode = "400", description = "Password is missing",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PatchMapping("/{doctorId}/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changeDoctorPassword(@PathVariable Long doctorId, @RequestBody ChangeDoctorPasswordCommand command) {
        doctorService.changeDoctorPassword(doctorId, command.password());
    }

    @Operation(summary = "Delete doctor by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Doctor deleted"),
            @ApiResponse(responseCode = "404", description = "Doctor not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    @DeleteMapping("/{doctorId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDoctor(@PathVariable Long doctorId) {
        doctorService.deleteDoctor(doctorId);
    }

    @Operation(summary = "Update doctor by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Doctor updated"),
            @ApiResponse(responseCode = "409", description = "Doctor with the provided email already exists",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PostMapping("/{doctorId}")
    public DoctorDto updateDoctorById(@PathVariable Long doctorId, @RequestBody UpdateDoctorCommand command) {
        return doctorService.updateDoctorById(doctorId, command);
    }
}
