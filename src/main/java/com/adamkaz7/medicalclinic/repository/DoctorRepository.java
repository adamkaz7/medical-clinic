package com.adamkaz7.medicalclinic.repository;

import com.adamkaz7.medicalclinic.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    boolean existsByEmail(String email);
}
