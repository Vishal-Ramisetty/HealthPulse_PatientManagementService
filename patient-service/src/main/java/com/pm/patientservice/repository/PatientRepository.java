package com.pm.patientservice.repository;

import com.pm.patientservice.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PatientRepository extends JpaRepository<Patient, UUID> {

    // This is a inbuild method provided by JPARespisotory that is being explicitly extracted
    boolean existsByEmail(String email);
    // Use this method to prevent bug in case other fields are updated for uuid that has same email
    boolean existsByEmailAndIdNot(String email,UUID id);
}
