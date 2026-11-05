package com.adamkaz7.medicalclinic.validator;

import com.adamkaz7.medicalclinic.command.CreateAppointmentCommand;
import com.adamkaz7.medicalclinic.exception.appointment.AppointmentAlreadyExistsException;
import com.adamkaz7.medicalclinic.exception.appointment.AppointmentValidationException;
import com.adamkaz7.medicalclinic.model.Appointment;
import com.adamkaz7.medicalclinic.model.Doctor;
import com.adamkaz7.medicalclinic.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class AppointmentValidator {
    private final AppointmentRepository appointmentRepository;

    public void validateAppointmentTime(LocalDateTime startTime, LocalDateTime endTime) {
        if (!endTime.isAfter(startTime)) {
            throw new AppointmentValidationException("Appointment end time must be after start time");
        }
        validateQuarterHour(startTime);
        validateQuarterHour(endTime);
    }

    public void validatePatientAvailability(Appointment appointment) {
        if (appointment.getPatient() != null) {
            throw new AppointmentValidationException("Appointment is already booked");
        }
        if (!appointment.getStartTime().isAfter(LocalDateTime.now())) {
            throw new AppointmentValidationException("Appointment has already started");
        }
    }

    public void validateDoctorAvailability(Doctor doctor, LocalDateTime startTime, LocalDateTime endTime) {
        if (appointmentRepository.existsByDoctor_IdAndStartTimeLessThanAndEndTimeGreaterThan(doctor.getId(), endTime, startTime)) {
            throw new AppointmentAlreadyExistsException(doctor.getId());
        }
    }

    private void validateQuarterHour(LocalDateTime time) {
        if (time.getMinute() % 15 != 0) {
            throw new AppointmentValidationException("Appointment time must be on a full quarter hour");
        }
    }
}
