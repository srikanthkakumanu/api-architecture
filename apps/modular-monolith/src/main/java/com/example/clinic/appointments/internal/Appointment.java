package com.example.clinic.appointments.internal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long patientId;

    @Column(nullable = false)
    private String patientName;

    @Column(nullable = false)
    private String doctorName;

    @Column(nullable = false)
    private LocalDateTime startsAt;

    @Column(nullable = false)
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentStatus status;

    protected Appointment() {
    }

    Appointment(Long patientId, String patientName, String doctorName, LocalDateTime startsAt, String reason) {
        this.patientId = patientId;
        this.patientName = patientName;
        this.doctorName = doctorName;
        this.startsAt = startsAt;
        this.reason = reason;
        this.status = AppointmentStatus.SCHEDULED;
    }

    Long getId() {
        return id;
    }

    Long getPatientId() {
        return patientId;
    }

    String getPatientName() {
        return patientName;
    }

    String getDoctorName() {
        return doctorName;
    }

    LocalDateTime getStartsAt() {
        return startsAt;
    }

    String getReason() {
        return reason;
    }

    AppointmentStatus getStatus() {
        return status;
    }
}
