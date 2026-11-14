
package com.example.hospitalmanager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MedicalRecordRequest(

        @NotNull
        Long patientId,

        @NotBlank
        @Size(max = 2000)
        String diagnosis,

        @NotBlank
        @Size(max = 2000)
        String prescription

) {}
