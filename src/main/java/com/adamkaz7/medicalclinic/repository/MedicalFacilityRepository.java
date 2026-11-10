package com.adamkaz7.medicalclinic.repository;

import com.adamkaz7.medicalclinic.model.MedicalFacility;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicalFacilityRepository extends JpaRepository<MedicalFacility,Long> {
    boolean existsByName(String name);
    boolean existsByIdAndDoctors_Id(Long facilityId, Long doctorId);
}
