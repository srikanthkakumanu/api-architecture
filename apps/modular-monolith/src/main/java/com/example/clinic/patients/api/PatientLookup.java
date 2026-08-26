package com.example.clinic.patients.api;

public interface PatientLookup {

    PatientSummary getPatient(long patientId);
}
