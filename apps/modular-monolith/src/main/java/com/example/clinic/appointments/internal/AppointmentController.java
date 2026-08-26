package com.example.clinic.appointments.internal;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/appointments")
class AppointmentController {

    private final AppointmentService appointments;

    AppointmentController(AppointmentService appointments) {
        this.appointments = appointments;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    AppointmentService.AppointmentResponse schedule(@Valid @RequestBody ScheduleAppointmentRequest request) {
        return appointments.schedule(new AppointmentService.ScheduleAppointmentCommand(
                request.patientId(),
                request.doctorName(),
                request.startsAt(),
                request.reason()
        ));
    }

    @GetMapping
    List<AppointmentService.AppointmentResponse> listAppointments() {
        return appointments.listAppointments();
    }

    record ScheduleAppointmentRequest(
            @NotNull Long patientId,
            @NotBlank String doctorName,
            @NotNull @Future LocalDateTime startsAt,
            @NotBlank String reason
    ) {
    }
}
