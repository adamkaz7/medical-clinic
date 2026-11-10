package com.adamkaz7.medicalclinic.service;

import com.adamkaz7.medicalclinic.command.CreateAppointmentCommand;
import com.adamkaz7.medicalclinic.dto.AppointmentDto;
import com.adamkaz7.medicalclinic.exception.appointment.AppointmentNotFoundException;
import com.adamkaz7.medicalclinic.exception.doctor.DoctorNotFoundException;
import com.adamkaz7.medicalclinic.exception.patient.PatientNotFoundException;
import com.adamkaz7.medicalclinic.mapper.AppointmentMapper;
import com.adamkaz7.medicalclinic.model.Appointment;
import com.adamkaz7.medicalclinic.model.Doctor;
import com.adamkaz7.medicalclinic.model.Patient;
import com.adamkaz7.medicalclinic.repository.AppointmentRepository;
import com.adamkaz7.medicalclinic.repository.DoctorRepository;
import com.adamkaz7.medicalclinic.repository.PatientRepository;
import com.adamkaz7.medicalclinic.validator.AppointmentValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final AppointmentMapper appointmentMapper;
    private final AppointmentValidator appointmentValidator;

    public AppointmentDto addAppointment(CreateAppointmentCommand command) {
        appointmentValidator.validateAppointmentTime(command.startTime(), command.endTime());
        Doctor doctor = doctorRepository.findById(command.doctorId())
                .orElseThrow(() -> new DoctorNotFoundException(command.doctorId()));
        appointmentValidator.validateDoctorAvailability(doctor, command.startTime(), command.endTime());
        Appointment appointment = appointmentMapper.toEntity(command);
        appointment.assignDoctor(doctor);
        appointment = appointmentRepository.save(appointment);
        return appointmentMapper.toDto(appointment);
    }

    public List<AppointmentDto> getAllAppointments() {
        return appointmentRepository
                .findAllByPatientIsNullAndStartTimeAfterOrderByStartTime(LocalDateTime.now()).stream()
                .map(appointmentMapper::toDto)
                .toList();
    }

    public void assignPatientToAppointment(Long appointmentId, Long patientId) {
        Appointment appointment = findAppointmentById(appointmentId);
        Patient patient = findPatientById(patientId);
        appointmentValidator.validatePatientAvailability(appointment);
        appointment.assignPatient(patient);
        appointmentRepository.save(appointment);
    }

    public AppointmentDto getAppointmentById(Long appointmentId) {
        Appointment appointment = findAppointmentById(appointmentId);
        return appointmentMapper.toDto(appointment);
    }

    public void deleteAppointment(Long appointmentId) {
        Appointment appointment = findAppointmentById(appointmentId);
        appointmentValidator.validatePatientAvailability(appointment);
        appointmentRepository.delete(appointment);
    }

    private Appointment findAppointmentById(Long appointmentId) {
        return appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException(appointmentId));
    }

    private Patient findPatientById(Long patientId) {
        return patientRepository.findById(patientId)
                .orElseThrow(() -> new PatientNotFoundException(patientId));
    }
}
