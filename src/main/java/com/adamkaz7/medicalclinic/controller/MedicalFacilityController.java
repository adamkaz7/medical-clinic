package com.adamkaz7.medicalclinic.controller;

import com.adamkaz7.medicalclinic.command.CreateMedicalFacilityCommand;
import com.adamkaz7.medicalclinic.command.UpdateMedicalFacilityCommand;
import com.adamkaz7.medicalclinic.dto.MedicalFacilityDto;
import com.adamkaz7.medicalclinic.service.MedicalFacilityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Facilities", description = "Facilities management")
@RestController
@RequestMapping("/facilities")
@RequiredArgsConstructor
public class MedicalFacilityController {
    private final MedicalFacilityService medicalFacilityService;

    @Operation(summary = "Add medical facility")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Medical facility created"),
            @ApiResponse(responseCode = "409", description = "Medical facility with the provided name already exists",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MedicalFacilityDto addMedicalFacility(@Valid @RequestBody CreateMedicalFacilityCommand command) {
        return medicalFacilityService.addMedicalFacility(command);
    }

    @Operation(summary = "Get medical facility by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Medical facility found"),
            @ApiResponse(responseCode = "404", description = "Medical facility not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    @GetMapping("/{facilityId}")
    public MedicalFacilityDto getMedicalFacilityById(@PathVariable Long facilityId) {
        return medicalFacilityService.getMedicalFacilityById(facilityId);
    }

    @Operation(summary = "Get all medical facilities")
    @ApiResponse(responseCode = "200", description = "Medical facility list")
    @GetMapping
    public List<MedicalFacilityDto> getAllMedicalFacilities() {
        return medicalFacilityService.getAllMedicalFacilities();
    }

    @Operation(summary = "Delete medical facility by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Medical facility deleted"),
            @ApiResponse(responseCode = "404", description = "Medical facility not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    @DeleteMapping("/{facilityId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMedicalFacilityById(@PathVariable Long facilityId) {
        medicalFacilityService.deleteMedicalFacility(facilityId);
    }

    @Operation(summary = "Update medical facility by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Medical facility updated"),
            @ApiResponse(responseCode = "409", description = "Medical facility with the provided name already exists",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PostMapping("/{facilityId}")
    public MedicalFacilityDto updateMedicalFacilityById(
            @PathVariable Long facilityId,
            @Valid @RequestBody UpdateMedicalFacilityCommand command) {
        return medicalFacilityService.updateMedicalFacility(facilityId, command);
    }
}
