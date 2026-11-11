package com.adamkaz7.medicalclinic.repository;

import com.adamkaz7.medicalclinic.model.Doctor;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    @Override
    @EntityGraph(attributePaths = "medicalFacilities")
    List<Doctor> findAll();
    boolean existsByEmail(String email);
}
