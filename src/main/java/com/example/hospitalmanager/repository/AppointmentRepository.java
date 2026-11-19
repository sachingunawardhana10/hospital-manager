package com.example.hospitalmanager.repository;

import com.example.hospitalmanager.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findAllByDoctor_Id(Long doctorId);

    List<Appointment> findAllByPatient_Id(Long patientId);

    boolean existsByPatient_Id(Long patientId);

    boolean existsBypatient_IdAndDoctor_Id(Long id, Long id1);

    boolean existsByPatient_IdAndDoctor_Id(Long id, Long id1);
}