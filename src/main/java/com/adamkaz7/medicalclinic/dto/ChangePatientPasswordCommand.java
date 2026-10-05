package com.adamkaz7.medicalclinic.dto;

public record ChangePatientPasswordCommand(String password) {
    @Override
    public String toString() {
        return "ChangePatientPasswordCommand{}";
    }
}
