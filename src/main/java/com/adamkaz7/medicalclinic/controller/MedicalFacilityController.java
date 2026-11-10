package com.adamkaz7.medicalclinic.controller;

import com.adamkaz7.medicalclinic.command.CreateMedicalFacilityCommand;
import com.adamkaz7.medicalclinic.command.UpdateMedicalFacilityCommand;
import com.adamkaz7.medicalclinic.dto.MedicalFacilityDto;
import com.adamkaz7.medicalclinic.service.MedicalFacilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/facilities")
@RequiredArgsConstructor
public class MedicalFacilityController {
    private final MedicalFacilityService medicalFacilityService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MedicalFacilityDto addMedicalFacility(@RequestBody CreateMedicalFacilityCommand command) {
        return medicalFacilityService.addMedicalFacility(command);
    }

    @GetMapping("/{facilityId}")
    public MedicalFacilityDto getMedicalFacilityById(@PathVariable Long facilityId) {
        return medicalFacilityService.getMedicalFacilityById(facilityId);
    }

    @GetMapping
    public List<MedicalFacilityDto> getAllMedicalFacilities() {
        return medicalFacilityService.getAllMedicalFacilities();
    }

    @DeleteMapping("/{facilityId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMedicalFacilityById(@PathVariable Long facilityId) {
        medicalFacilityService.deleteMedicalFacility(facilityId);
    }

    @PostMapping("/{facilityId}")
    public MedicalFacilityDto updateMedicalFacilityById(
            @PathVariable Long facilityId,
            @RequestBody UpdateMedicalFacilityCommand command) {
        return medicalFacilityService.updateMedicalFacility(facilityId, command);
    }
}
