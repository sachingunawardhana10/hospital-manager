package com.example.hospitalmanager.dto;

import java.time.LocalDate;

public record PatientResponse(
        Long id,
        String name,
        String email,
        String phone,
        LocalDate dateOfBirth,
        String address
) {}