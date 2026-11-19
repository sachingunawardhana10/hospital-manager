package com.example.hospitalmanager.service;

import com.example.hospitalmanager.dto.MedicalRecordRequest;
import com.example.hospitalmanager.dto.MedicalRecordResponse;
import com.example.hospitalmanager.exception.ApiException;
import com.example.hospitalmanager.model.AppUser;
import com.example.hospitalmanager.model.MedicalRecord;
import com.example.hospitalmanager.model.Patient;
import com.example.hospitalmanager.model.Role;
import com.example.hospitalmanager.repository.AppointmentRepository;
import com.example.hospitalmanager.repository.MedicalRecordRepository;
import com.example.hospitalmanager.repository.PatientRepository;
import com.example.hospitalmanager.repository.UserRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;
    private final AppointmentRepository appointmentRepository;

    public MedicalRecordService(
            MedicalRecordRepository medicalRecordRepository,
            PatientRepository patientRepository,
            UserRepository userRepository,
            AppointmentRepository appointmentRepository) {

        this.medicalRecordRepository = medicalRecordRepository;
        this.patientRepository = patientRepository;
        this.userRepository = userRepository;
        this.appointmentRepository = appointmentRepository;
    }

    @Transactional
    public MedicalRecordResponse createRecord(
            String username,
            MedicalRecordRequest request) {

        AppUser doctor = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND, "Doctor not found"));

        if (doctor.getRole() != Role.DOCTOR) {
            throw new ApiException(
                    HttpStatus.FORBIDDEN, "Only doctors can create medical records");
        }

        Patient patient = patientRepository.findById(request.patientId())
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND, "Patient not found"));

        boolean hasAppointment =
                appointmentRepository.existsByPatient_IdAndDoctor_Id(
                        patient.getId(), doctor.getId());

        if (!hasAppointment) {
            throw new ApiException(
                    HttpStatus.FORBIDDEN,
                    "You are not assigned to this patient");
        }

        MedicalRecord record = new MedicalRecord();
        record.setPatient(patient);
        record.setDoctor(doctor);
        record.setDiagnosis(request.diagnosis());
        record.setPrescription(request.prescription());

        return convertToResponse(medicalRecordRepository.save(record));
    }

    @Transactional(readOnly = true)
    public List<MedicalRecordResponse> getPatientRecords(
            String username, Long patientId) {

        AppUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND, "User not found"));

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND, "Patient not found"));

        if (user.getRole() == Role.DOCTOR) {

            boolean hasAppointment =
                    appointmentRepository.existsBypatient_IdAndDoctor_Id(
                            patient.getId(), user.getId());

            if (!hasAppointment) {
                throw new ApiException(
                        HttpStatus.FORBIDDEN,
                        "You are not assigned to this patient");
            }

        } else if (user.getRole() != Role.ADMIN) {

            throw new ApiException(
                    HttpStatus.FORBIDDEN,
                    "Access denied");
        }

        return medicalRecordRepository.findAllByPatient_Id(patientId)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    private MedicalRecordResponse convertToResponse(
            MedicalRecord record) {

        return new MedicalRecordResponse(
                record.getId(),
                record.getPatient().getId(),
                record.getPatient().getName(),
                record.getDoctor().getId(),
                record.getDoctor().getUsername(),
                record.getDiagnosis(),
                record.getPrescription()
        );
    }
}