package com.adamkaz7.medicalclinic.model;

import com.adamkaz7.medicalclinic.command.UpdateMedicalFacilityCommand;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
@Entity
@Builder
@Table(name = "MEDICAL_FACILITIES")
public class MedicalFacility {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String name;
    @Column(nullable = false)
    private String city;
    @Column(name = "POSTAL_CODE", nullable = false)
    private String postalCode;
    @Column(nullable = false)
    private String street;
    @Column(name = "BUILDING_NUMBER", nullable = false)
    private String buildingNumber;
    @ToString.Exclude
    @ManyToMany
    @JoinTable(
            name = "DOCTOR_FACILITIES",
            joinColumns = @JoinColumn(name = "FACILITY_ID"),
            inverseJoinColumns = @JoinColumn(name = "DOCTOR_ID")
    )
    private Set<Doctor> doctors = new HashSet<>();

    public void assignDoctor(Doctor doctor) {
        doctors.add(doctor);
    }

    public void updateMedicalFacility(UpdateMedicalFacilityCommand command) {
        this.name = command.name();
        this.city = command.city();
        this.postalCode = command.postalCode();
        this.street = command.street();
        this.buildingNumber = command.buildingNumber();
    }

    public void removeDoctor(Doctor doctor) {
        doctors.remove(doctor);
    }
}