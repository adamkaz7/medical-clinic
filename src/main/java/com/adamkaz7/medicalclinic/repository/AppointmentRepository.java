package com.adamkaz7.medicalclinic.repository;

import com.adamkaz7.medicalclinic.model.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    @EntityGraph(attributePaths = "doctor.medicalFacilities")
    Page<Appointment> findAllByPatientIsNullAndStartTimeAfterOrderByStartTime(LocalDateTime now, Pageable pageable);
    boolean existsByDoctor_IdAndStartTimeLessThanAndEndTimeGreaterThan(Long doctorId, LocalDateTime startTime, LocalDateTime endTime);
}
