package com.example.clinic.patients.internal;

import org.springframework.data.jpa.repository.JpaRepository;

interface PatientRepository extends JpaRepository<Patient, Long> {

    boolean existsByEmailIgnoreCase(String email);
}
