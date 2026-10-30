package com.adamkaz7.medicalclinic.model;

import com.adamkaz7.medicalclinic.command.UpdatePatientCommand;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @Column(name = "BIRTHDAY")
    private LocalDate birthday;
    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    public Patient(
            String email,
            String password,
            String idCardNo,
            String firstName,
            String lastName,
            String phoneNumber,
            LocalDate birthday
    ) {
        this(null, email, password, idCardNo, firstName, lastName, phoneNumber, birthday, null);
    }

    public void changePassword(String newPassword) {
        this.password = newPassword;
    }

    public void updatePatient(UpdatePatientCommand command) {
        this.password = command.password();
        this.firstName = command.firstName();
        this.lastName = command.lastName();
        this.phoneNumber = command.phoneNumber();
        this.birthday = command.birthday();
    }

    public void assignUser(User user) {
        this.user = user;
    }
}
