package com.adamkaz7.medicalclinic.repository;

import com.adamkaz7.medicalclinic.model.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    @EntityGraph(attributePaths = "user")
    Page<Patient> findAll(Pageable pageable);
    @EntityGraph(attributePaths = "user")
    Optional<Patient> findByUser_Email(String email);
    boolean existsByUser_Id(Long userid);
    boolean existsByIdCardNo(String idCardNo);
}
