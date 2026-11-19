
package com.example.hospitalmanager.service;

import com.example.hospitalmanager.dto.AppointmentRequest;
import com.example.hospitalmanager.dto.AppointmentResponse;
import com.example.hospitalmanager.exception.ApiException;
import com.example.hospitalmanager.model.Appointment;
import com.example.hospitalmanager.model.AppointmentStatus;
import com.example.hospitalmanager.model.AppUser;
import com.example.hospitalmanager.model.Patient;
import com.example.hospitalmanager.model.Role;
import com.example.hospitalmanager.repository.AppointmentRepository;
import com.example.hospitalmanager.repository.PatientRepository;
import com.example.hospitalmanager.repository.UserRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;

    public AppointmentService(
            AppointmentRepository appointmentRepository,
            PatientRepository patientRepository,
            UserRepository userRepository) {

        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public AppointmentResponse bookAppointment(
            String username,
            AppointmentRequest request) {

        Patient patient = patientRepository.findByUser_Username(username)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND,
                        "Patient profile not found"
                ));

        AppUser doctor = userRepository.findById(request.doctorId())
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND,
                        "Doctor not found"
                ));

        if (doctor.getRole() != Role.DOCTOR) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "Selected user is not a doctor"
            );
        }

        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setAppointmentDateTime(request.appointmentDateTime());
        appointment.setStatus(AppointmentStatus.SCHEDULED);

        Appointment savedAppointment =
                appointmentRepository.save(appointment);

        return convertToResponse(savedAppointment);
    }

    @Transactional(readOnly = true)
    public List<AppointmentResponse> getDoctorAppointments(String username) {

        AppUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND, "User not found"));

        if (user.getRole() == Role.ADMIN) {
            return appointmentRepository.findAll()
                    .stream()
                    .map(this::convertToResponse)
                    .toList();
        }

        if (user.getRole() != Role.DOCTOR) {
            throw new ApiException(
                    HttpStatus.FORBIDDEN,
                    "Only doctors and administrators can view appointments");
        }

        return appointmentRepository.findAllByDoctor_Id(user.getId())
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AppointmentResponse> getPatientAppointments(String username) {

        Patient patient = patientRepository.findByUser_Username(username)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND,
                        "Patient profile not found"
                ));

        return appointmentRepository.findAllByPatient_Id(patient.getId())
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    private AppointmentResponse convertToResponse(Appointment appointment) {

        return new AppointmentResponse(
                appointment.getId(),
                appointment.getPatient().getId(),
                appointment.getPatient().getName(),
                appointment.getDoctor().getId(),
                appointment.getDoctor().getUsername(),
                appointment.getAppointmentDateTime(),
                appointment.getStatus()
        );
    }
}
