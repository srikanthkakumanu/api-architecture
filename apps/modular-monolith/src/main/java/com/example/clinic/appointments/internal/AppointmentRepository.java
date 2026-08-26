package com.example.clinic.appointments.internal;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    boolean existsByDoctorNameIgnoreCaseAndStartsAt(String doctorName, LocalDateTime startsAt);

    List<Appointment> findAllByOrderByStartsAtAsc();
}
