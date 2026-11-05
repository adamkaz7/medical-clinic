package com.adamkaz7.medicalclinic.mapper;

import com.adamkaz7.medicalclinic.command.CreateAppointmentCommand;
import com.adamkaz7.medicalclinic.dto.AppointmentDto;
import com.adamkaz7.medicalclinic.model.Appointment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {
    Appointment toEntity(CreateAppointmentCommand command);
    AppointmentDto toDto(Appointment appointment);
}
