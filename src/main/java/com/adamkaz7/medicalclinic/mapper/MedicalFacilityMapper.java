package com.adamkaz7.medicalclinic.mapper;

import com.adamkaz7.medicalclinic.command.CreateMedicalFacilityCommand;
import com.adamkaz7.medicalclinic.dto.MedicalFacilityDto;
import com.adamkaz7.medicalclinic.model.MedicalFacility;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MedicalFacilityMapper {
    MedicalFacility toEntity(CreateMedicalFacilityCommand command);
    MedicalFacilityDto toDto(MedicalFacility medicalFacility);
}
