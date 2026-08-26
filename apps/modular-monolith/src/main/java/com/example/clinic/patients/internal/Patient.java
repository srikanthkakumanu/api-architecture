package com.example.clinic.patients.internal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "patients")
class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    protected Patient() {
    }

    Patient(String fullName, String email, String phoneNumber, LocalDate dateOfBirth) {
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
    }

    Long getId() {
        return id;
    }

    String getFullName() {
        return fullName;
    }

    String getEmail() {
        return email;
    }

    String getPhoneNumber() {
        return phoneNumber;
    }

    LocalDate getDateOfBirth() {
        return dateOfBirth;
    }
}
