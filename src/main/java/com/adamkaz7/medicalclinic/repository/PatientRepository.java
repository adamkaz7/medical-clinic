package com.adamkaz7.medicalclinic.repository;

import com.adamkaz7.medicalclinic.model.Patient;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    @Override
    @EntityGraph(attributePaths = "user")
    List<Patient> findAll();
    @EntityGraph(attributePaths = "user")
    Optional<Patient> findByUser_Email(String email);
    boolean existsByUser_Id(Long userid);
    boolean existsByIdCardNo(String idCardNo);
}
