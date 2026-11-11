package com.adamkaz7.medicalclinic.model;

import com.adamkaz7.medicalclinic.command.UpdateDoctorCommand;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
@Entity
@Builder
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
    @ManyToMany(mappedBy = "doctors", fetch = FetchType.LAZY)
    private Set<MedicalFacility> medicalFacilities = new HashSet<>();

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
