
package com.example.hospitalmanager.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record AppointmentRequest(

        @NotNull
        Long doctorId,

        @NotNull
        @Future
        LocalDateTime appointmentDateTime

) {}
