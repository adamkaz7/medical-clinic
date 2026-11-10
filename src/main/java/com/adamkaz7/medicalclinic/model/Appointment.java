package com.adamkaz7.medicalclinic.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
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

    public void assignPatient(Patient patient) {
        this.patient = patient;
    }

    public void assignDoctor(Doctor doctor) {
        this.doctor = doctor;
    }
}
