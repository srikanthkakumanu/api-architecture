package com.example.clinic.patients.internal;

import com.example.clinic.patients.api.PatientLookup;
import com.example.clinic.patients.api.PatientSummary;
import com.example.clinic.shared.api.BusinessRuleException;
import com.example.clinic.shared.api.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
class PatientService implements PatientLookup {

    private final PatientRepository patients;

    PatientService(PatientRepository patients) {
        this.patients = patients;
    }

    @Transactional
    PatientResponse register(RegisterPatientCommand command) {
        if (patients.existsByEmailIgnoreCase(command.email())) {
            throw new BusinessRuleException("A patient with this email already exists.");
        }

        var patient = patients.save(new Patient(
                command.fullName(),
                command.email(),
                command.phoneNumber(),
                command.dateOfBirth()
        ));

        return PatientResponse.from(patient);
    }

    List<PatientResponse> listPatients() {
        return patients.findAll().stream()
                .map(PatientResponse::from)
                .toList();
    }

    @Override
    public PatientSummary getPatient(long patientId) {
        return patients.findById(patientId)
                .map(patient -> new PatientSummary(patient.getId(), patient.getFullName(), patient.getEmail()))
                .orElseThrow(() -> new NotFoundException("Patient %d was not found.".formatted(patientId)));
    }

    record RegisterPatientCommand(String fullName, String email, String phoneNumber, LocalDate dateOfBirth) {
    }

    record PatientResponse(Long id, String fullName, String email, String phoneNumber, LocalDate dateOfBirth) {

        static PatientResponse from(Patient patient) {
            return new PatientResponse(
                    patient.getId(),
                    patient.getFullName(),
                    patient.getEmail(),
                    patient.getPhoneNumber(),
                    patient.getDateOfBirth()
            );
        }
    }
}
