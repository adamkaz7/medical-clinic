package com.adamkaz7.medicalclinic.repository;

import com.adamkaz7.medicalclinic.model.Appointment;
import com.adamkaz7.medicalclinic.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findAllByPatientIsNullAndStartTimeAfterOrderByStartTime(LocalDateTime now);
    List<Appointment> findAllByDoctor(Doctor doctor);
    boolean existsByDoctor_IdAndStartTimeLessThanAndEndTimeGreaterThan(Long doctorId, LocalDateTime startTime, LocalDateTime endTime);
}
