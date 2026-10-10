package com.adamkaz7.medicalclinic.repository;

import com.adamkaz7.medicalclinic.model.Patient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class PatientRepository {
    private final Map<String, Patient> patients = new ConcurrentHashMap<>();

    public List<Patient> findAll() {
        return List.copyOf(patients.values());
    }

    public Optional<Patient> findByEmail(String email) {
        return Optional.ofNullable(patients.get(email));
    }

    public Optional<Patient> add(Patient patient) {
        Patient existingPatient = patients.putIfAbsent(patient.getEmail(), patient);
        if (existingPatient != null) {
            return Optional.empty();
        }
        return Optional.of(patient);
    }

    public boolean deleteByEmail(String email) {
        Patient removedPatient = patients.remove(email);
        return removedPatient != null;
    }

    public Optional<Patient> update(Patient patient) {
        Patient previousPatient = patients.replace(patient.getEmail(), patient);
        if (previousPatient == null) {
            return Optional.empty();
        }
        return Optional.of(patient);
    }
}
