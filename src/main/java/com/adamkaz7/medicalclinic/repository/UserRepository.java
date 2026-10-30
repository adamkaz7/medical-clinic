package com.adamkaz7.medicalclinic.repository;

import com.adamkaz7.medicalclinic.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
}