package com.adamkaz7.medicalclinic.dto;

import java.time.LocalDateTime;

public record AppointmentDto(
        Long id,
        LocalDateTime startTime,
        LocalDateTime endTime,
        DoctorDto doctor,
        PatientDto patient
) {
}
