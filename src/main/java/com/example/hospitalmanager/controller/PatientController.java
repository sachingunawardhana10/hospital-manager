package com.example.hospitalmanager.controller;

import com.example.hospitalmanager.dto.PatientResponse;
import com.example.hospitalmanager.model.Patient;
import com.example.hospitalmanager.service.PatientService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PatientResponse>> getAllPatients() {

        List<PatientResponse> patients =
                patientService.getAllPatients()
                        .stream()
                        .map(this::convertToResponse)
                        .toList();

        return ResponseEntity.ok(patients);
    }

    private PatientResponse convertToResponse(Patient patient) {
        return new PatientResponse(
                patient.getId(),
                patient.getName(),
                patient.getEmail(),
                patient.getPhone(),
                patient.getDateOfBirth(),
                patient.getAddress()
        );
    }
}