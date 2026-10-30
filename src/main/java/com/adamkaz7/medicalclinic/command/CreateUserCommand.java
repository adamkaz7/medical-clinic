package com.adamkaz7.medicalclinic.command;

public record CreateUserCommand(String email, String password, String firstName, String lastName) {
}
