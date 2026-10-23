package com.adamkaz7.medicalclinic.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@ToString
@Entity
@Table(name = "patients")
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(nullable = false, unique = true)
    private String email;
    @ToString.Exclude
    private String password;
    @Column(name = "ID_CARD", nullable = false, unique = true)
    private String idCardNo;
    @Column(name = "FIRST_NAME")
    private String firstName;
    @Column(name = "LAST_NAME")
    private String lastName;
    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;
    @Column
    private LocalDate birthday;

    public Patient(
            String email,
            String password,
            String idCardNo,
            String firstName,
            String lastName,
            String phoneNumber,
            LocalDate birthday
    ) {
        this(null, email, password, idCardNo, firstName, lastName, phoneNumber, birthday);
    }

    public Patient withPassword(String newPassword) {
        return new Patient(
                id,
                email,
                newPassword,
                idCardNo,
                firstName,
                lastName,
                phoneNumber,
                birthday
        );
    }

    public void updatePatient(Patient updatedPatient) {
        this.password = updatedPatient.getPassword();
        this.idCardNo = updatedPatient.getIdCardNo();
        this.firstName = updatedPatient.getFirstName();
        this.lastName = updatedPatient.getLastName();
        this.phoneNumber = updatedPatient.getPhoneNumber();
        this.birthday = updatedPatient.getBirthday();
    }
}
