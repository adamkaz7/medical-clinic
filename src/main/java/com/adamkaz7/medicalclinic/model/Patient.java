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
@Table(name = "PATIENTS")
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "ID_CARD", nullable = false, unique = true)
    private String idCardNo;
    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;
    @Column(name = "BIRTHDAY")
    private LocalDate birthday;
    @OneToOne
    @JoinColumn(name = "USER_ID", nullable = false, unique = true)
    private User user;

    public void updatePatient(UpdatePatientCommand command) {
        this.phoneNumber = command.phoneNumber();
        this.birthday = command.birthday();
    }

    public void assignUser(User user) {
        this.user = user;
    }
}
