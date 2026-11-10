package com.adamkaz7.medicalclinic.model;

import com.adamkaz7.medicalclinic.command.UpdateDoctorCommand;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Entity
@Table(name = "DOCTORS")
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String email;
    @ToString.Exclude
    @Column(nullable = false)
    private String password;
    @Column (name = "FIRST_NAME", nullable = false)
    private String firstName;
    @Column (name = "LAST_NAME",  nullable = false)
    private String lastName;
    @Column(nullable = false)
    private String specialization;
    @ToString.Exclude
    @ManyToMany(mappedBy = "doctors")
    private Set<MedicalFacility> medicalFacilities = new HashSet<>();

    public Doctor(Long id, String email, String password, String firstName, String lastName, String specialization) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.specialization = specialization;
    }

    public void changePassword(String newPassword) {
        this.password = newPassword;
    }

    public void updateDoctor(UpdateDoctorCommand command) {
        this.email = command.email();
        this.password = command.password();
        this.firstName = command.firstName();
        this.lastName = command.lastName();
        this.specialization = command.specialization();
    }
}
