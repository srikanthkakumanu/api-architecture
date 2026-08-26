package com.example.clinic.patients.internal;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/patients")
class PatientController {

    private final PatientService patients;

    PatientController(PatientService patients) {
        this.patients = patients;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    PatientService.PatientResponse register(@Valid @RequestBody RegisterPatientRequest request) {
        return patients.register(new PatientService.RegisterPatientCommand(
                request.fullName(),
                request.email(),
                request.phoneNumber(),
                request.dateOfBirth()
        ));
    }

    @GetMapping
    List<PatientService.PatientResponse> listPatients() {
        return patients.listPatients();
    }

    record RegisterPatientRequest(
            @NotBlank String fullName,
            @NotBlank @Email String email,
            @NotBlank String phoneNumber,
            @NotNull @Past LocalDate dateOfBirth
    ) {
    }
}
