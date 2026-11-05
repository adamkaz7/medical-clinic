package com.adamkaz7.medicalclinic.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Entity
@Table(name = "APPOINTMENTS")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "START_TIME", nullable = false)
    private LocalDateTime startTime;
    @Column(name = "END_TIME", nullable = false)
    private LocalDateTime endTime;
    @ManyToOne(optional = false)
    @JoinColumn(name = "DOCTOR_ID", nullable = false)
    private Doctor doctor;
    @ManyToOne
    @JoinColumn(name = "PATIENT_ID")
    private Patient patient;

    public Appointment(Long id, LocalDateTime startTime, LocalDateTime endTime, Doctor doctor, Patient patient) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.doctor = doctor;
        this.patient = patient;
    }

    public void assignPatient(Patient patient) {
        this.patient = patient;
    }

    public void assignDoctor(Doctor doctor) {
        this.doctor = doctor;
    }
}
