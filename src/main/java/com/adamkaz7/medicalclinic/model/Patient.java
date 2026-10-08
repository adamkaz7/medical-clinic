package com.adamkaz7.medicalclinic.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@Builder
@ToString
public class Patient {
    private final String email;
    @ToString.Exclude
    private final String password;
    private final String idCardNo;
    private final String firstName;
    private final String lastName;
    private final String phoneNumber;
    private final LocalDate birthday;

    public Patient withPassword(String newPassword) {
        return new Patient(
                email,
                newPassword,
                idCardNo,
                firstName,
                lastName,
                phoneNumber,
                birthday
        );
    }
}
