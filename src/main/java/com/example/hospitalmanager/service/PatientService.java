
package com.example.hospitalmanager.service;

import com.example.hospitalmanager.exception.ApiException;
import com.example.hospitalmanager.model.Patient;
import com.example.hospitalmanager.repository.PatientRepository;
import com.example.hospitalmanager.repository.AppointmentRepository;
import com.example.hospitalmanager.repository.MedicalRecordRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final MedicalRecordRepository medicalRecordRepository;

    public PatientService(
            PatientRepository patientRepository,
            AppointmentRepository appointmentRepository,
            MedicalRecordRepository medicalRecordRepository) {

        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
        this.medicalRecordRepository = medicalRecordRepository;
    }

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Patient getPatientById(Long id) {

        return patientRepository.findById(id)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND,
                        "Patient not found"
                ));
    }

    @Transactional
    public Patient createPatient(Patient patient) {

        patient.setId(null);

        return patientRepository.save(patient);
    }

    @Transactional
    public Patient updatePatient(Long id, Patient updatedPatient) {

        Patient existingPatient = getPatientById(id);

        existingPatient.setName(updatedPatient.getName());
        existingPatient.setEmail(updatedPatient.getEmail());
        existingPatient.setPhone(updatedPatient.getPhone());
        existingPatient.setDateOfBirth(updatedPatient.getDateOfBirth());
        existingPatient.setAddress(updatedPatient.getAddress());

        return patientRepository.save(existingPatient);
    }

    @Transactional
    public void deletePatient(Long id) {

        Patient patient = getPatientById(id);

        boolean hasAppointments =
                appointmentRepository.existsByPatient_Id(id);

        boolean hasMedicalRecords =
                medicalRecordRepository.existsByPatient_Id(id);

        if (hasAppointments || hasMedicalRecords) {
            throw new ApiException(
                    HttpStatus.CONFLICT,
                    "Cannot delete a patient who has appointments or medical records"
            );
        }

        patientRepository.delete(patient);
    }
}
