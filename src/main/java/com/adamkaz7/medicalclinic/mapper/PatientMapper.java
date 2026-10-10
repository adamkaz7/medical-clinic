package com.adamkaz7.medicalclinic.mapper;

import com.adamkaz7.medicalclinic.command.CreatePatientCommand;
import com.adamkaz7.medicalclinic.command.UpdatePatientCommand;
import com.adamkaz7.medicalclinic.model.Patient;
import com.adamkaz7.medicalclinic.dto.PatientDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PatientMapper {
    PatientDto toDto(Patient patient);
    Patient toEntity(CreatePatientCommand command);
    Patient toEntity(UpdatePatientCommand command);
}
