package com.adamkaz7.medicalclinic.command;

public record ChangePatientPasswordCommand(String password) {
    @Override
    public String toString() {
        return "ChangePatientPasswordCommand{}";
    }
}
