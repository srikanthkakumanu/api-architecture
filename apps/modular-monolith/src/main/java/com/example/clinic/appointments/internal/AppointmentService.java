package com.example.clinic.appointments.internal;

import com.example.clinic.patients.api.PatientLookup;
import com.example.clinic.shared.api.BusinessRuleException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
class AppointmentService {

    private final AppointmentRepository appointments;
    private final PatientLookup patients;

    AppointmentService(AppointmentRepository appointments, PatientLookup patients) {
        this.appointments = appointments;
        this.patients = patients;
    }

    @Transactional
    AppointmentResponse schedule(ScheduleAppointmentCommand command) {
        if (appointments.existsByDoctorNameIgnoreCaseAndStartsAt(command.doctorName(), command.startsAt())) {
            throw new BusinessRuleException("The doctor already has an appointment at this time.");
        }

        var patient = patients.getPatient(command.patientId());
        var appointment = appointments.save(new Appointment(
                patient.id(),
                patient.fullName(),
                command.doctorName(),
                command.startsAt(),
                command.reason()
        ));

        return AppointmentResponse.from(appointment);
    }

    List<AppointmentResponse> listAppointments() {
        return appointments.findAllByOrderByStartsAtAsc().stream()
                .map(AppointmentResponse::from)
                .toList();
    }

    record ScheduleAppointmentCommand(Long patientId, String doctorName, LocalDateTime startsAt, String reason) {
    }

    record AppointmentResponse(
            Long id,
            Long patientId,
            String patientName,
            String doctorName,
            LocalDateTime startsAt,
            String reason,
            AppointmentStatus status
    ) {

        static AppointmentResponse from(Appointment appointment) {
            return new AppointmentResponse(
                    appointment.getId(),
                    appointment.getPatientId(),
                    appointment.getPatientName(),
                    appointment.getDoctorName(),
                    appointment.getStartsAt(),
                    appointment.getReason(),
                    appointment.getStatus()
            );
        }
    }
}
