package com.adamkaz7.medicalclinic.service;

import com.adamkaz7.medicalclinic.command.CreateMedicalFacilityCommand;
import com.adamkaz7.medicalclinic.command.UpdateMedicalFacilityCommand;
import com.adamkaz7.medicalclinic.dto.MedicalFacilityDto;
import com.adamkaz7.medicalclinic.exception.facility.MedicalFacilityAlreadyExistsException;
import com.adamkaz7.medicalclinic.exception.facility.MedicalFacilityNotFoundException;
import com.adamkaz7.medicalclinic.mapper.MedicalFacilityMapper;
import com.adamkaz7.medicalclinic.model.MedicalFacility;
import com.adamkaz7.medicalclinic.repository.MedicalFacilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicalFacilityService {
    private final MedicalFacilityRepository medicalFacilityRepository;
    private final MedicalFacilityMapper medicalFacilityMapper;

    public MedicalFacilityDto addMedicalFacility(CreateMedicalFacilityCommand command) {
        if (medicalFacilityRepository.existsByName(command.name())) {
            throw new MedicalFacilityAlreadyExistsException(command.name());
        }
        MedicalFacility facility = medicalFacilityMapper.toEntity(command);
        facility = medicalFacilityRepository.save(facility);
        return medicalFacilityMapper.toDto(facility);
    }

    public List<MedicalFacilityDto> getAllMedicalFacilities() {
        return medicalFacilityRepository.findAll().stream()
                .map(medicalFacilityMapper::toDto)
                .toList();
    }

    public MedicalFacilityDto getMedicalFacilityById(Long facilityId) {
        MedicalFacility facility = medicalFacilityRepository.findById(facilityId)
                .orElseThrow(() -> new MedicalFacilityNotFoundException(facilityId));
        return medicalFacilityMapper.toDto(facility);
    }

    public MedicalFacilityDto updateMedicalFacility(Long facilityId, UpdateMedicalFacilityCommand command) {
        MedicalFacility facility = findMedicalFacilityById(facilityId);
        if (!facility.getName().equals(command.name()) && medicalFacilityRepository.existsByName(command.name())) {
            throw new MedicalFacilityAlreadyExistsException(command.name());
        }
        facility.updateMedicalFacility(command);
        facility = medicalFacilityRepository.save(facility);
        return medicalFacilityMapper.toDto(facility);
    }

    public void deleteMedicalFacility(Long facilityId) {
        MedicalFacility facility = findMedicalFacilityById(facilityId);
        medicalFacilityRepository.delete(facility);
    }

    private MedicalFacility findMedicalFacilityById(Long facilityId) {
        return medicalFacilityRepository.findById(facilityId)
                .orElseThrow(() -> new MedicalFacilityNotFoundException(facilityId));
    }
}
