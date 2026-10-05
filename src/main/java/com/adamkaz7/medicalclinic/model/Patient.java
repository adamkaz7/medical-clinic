package com.adamkaz7.medicalclinic.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
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
}
