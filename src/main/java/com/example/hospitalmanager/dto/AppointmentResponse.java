
package com.example.hospitalmanager.dto;

import com.example.hospitalmanager.model.AppointmentStatus;

import java.time.LocalDateTime;

public record AppointmentResponse(

        Long id,

        Long patientId,

        String patientName,

        Long doctorId,

        String doctorUsername,

        LocalDateTime appointmentDateTime,

        AppointmentStatus status

) {}
