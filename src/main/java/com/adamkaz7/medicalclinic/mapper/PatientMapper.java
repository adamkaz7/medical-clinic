package com.adamkaz7.medicalclinic.mapper;

import com.adamkaz7.medicalclinic.command.CreatePatientCommand;
import com.adamkaz7.medicalclinic.model.Patient;
import com.adamkaz7.medicalclinic.dto.PatientDto;
import org.springframework.stereotype.Component;

@Component
public class PatientMapper {
    public PatientDto toDto(Patient patient) {
        return PatientDto.builder()
                .email(patient.getEmail())
                .idCardNo(patient.getIdCardNo())
                .firstName(patient.getFirstName())
                .lastName(patient.getLastName())
                .phoneNumber(patient.getPhoneNumber())
                .birthday(patient.getBirthday())
                .build();
    }

    public Patient toPatient(CreatePatientCommand command) {
        return Patient.builder()
                .email(command.email())
                .password(command.password())
                .idCardNo(command.idCardNo())
                .firstName(command.firstName())
                .lastName(command.lastName())
                .phoneNumber(command.phoneNumber())
                .birthday(command.birthday())
                .build();
    }
}
