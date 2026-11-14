
package com.example.hospitalmanager.dto;

public record MedicalRecordResponse(

        Long id,

        Long patientId,

        String patientName,

        Long doctorId,

        String doctorUsername,

        String diagnosis,

        String prescription

) {}
