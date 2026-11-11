package com.adamkaz7.medicalclinic.repository;

import com.adamkaz7.medicalclinic.model.Appointment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    @EntityGraph(attributePaths = "doctor.medicalFacilities")
    List<Appointment> findAllByPatientIsNullAndStartTimeAfterOrderByStartTime(LocalDateTime now);
    boolean existsByDoctor_IdAndStartTimeLessThanAndEndTimeGreaterThan(Long doctorId, LocalDateTime startTime, LocalDateTime endTime);
}
