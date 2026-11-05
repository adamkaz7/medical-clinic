package com.adamkaz7.medicalclinic.mapper;

import com.adamkaz7.medicalclinic.command.CreateDoctorCommand;
import com.adamkaz7.medicalclinic.dto.DoctorDto;
import com.adamkaz7.medicalclinic.model.Doctor;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DoctorMapper {
    Doctor toEntity(CreateDoctorCommand command);
    DoctorDto toDto(Doctor doctor);
}

