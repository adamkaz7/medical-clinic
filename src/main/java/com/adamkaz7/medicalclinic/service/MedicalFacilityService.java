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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Pageable;

@Service
@RequiredArgsConstructor
@Slf4j
public class MedicalFacilityService {
    private final MedicalFacilityRepository medicalFacilityRepository;
    private final MedicalFacilityMapper medicalFacilityMapper;

    @Transactional
    public MedicalFacilityDto addMedicalFacility(CreateMedicalFacilityCommand command) {
        if (medicalFacilityRepository.existsByName(command.name())) {
            throw new MedicalFacilityAlreadyExistsException(command.name());
        }
        log.info("Creating medical facility: name={}", command.name());
        MedicalFacility facility = medicalFacilityMapper.toEntity(command);
        facility = medicalFacilityRepository.save(facility);
        return medicalFacilityMapper.toDto(facility);
    }

    @Transactional
    public Page<MedicalFacilityDto> getAllMedicalFacilities(Pageable pageable) {
        return medicalFacilityRepository.findAll(pageable)
                .map(medicalFacilityMapper::toDto);
    }

    public MedicalFacilityDto getMedicalFacilityById(Long facilityId) {
        MedicalFacility facility = medicalFacilityRepository.findById(facilityId)
                .orElseThrow(() -> new MedicalFacilityNotFoundException(facilityId));
        return medicalFacilityMapper.toDto(facility);
    }

    @Transactional
    public MedicalFacilityDto updateMedicalFacility(Long facilityId, UpdateMedicalFacilityCommand command) {
        MedicalFacility facility = findMedicalFacilityById(facilityId);
        if (!facility.getName().equals(command.name()) && medicalFacilityRepository.existsByName(command.name())) {
            throw new MedicalFacilityAlreadyExistsException(command.name());
        }
        log.info("Updating medical facility: facilityId={}", facilityId);
        facility.updateMedicalFacility(command);
        facility = medicalFacilityRepository.save(facility);
        return medicalFacilityMapper.toDto(facility);
    }

    @Transactional
    public void deleteMedicalFacility(Long facilityId) {
        MedicalFacility facility = findMedicalFacilityById(facilityId);
        log.info("Deleting medical facility: facilityId={}", facilityId);
        medicalFacilityRepository.delete(facility);
    }

    private MedicalFacility findMedicalFacilityById(Long facilityId) {
        return medicalFacilityRepository.findById(facilityId)
                .orElseThrow(() -> new MedicalFacilityNotFoundException(facilityId));
    }
}
